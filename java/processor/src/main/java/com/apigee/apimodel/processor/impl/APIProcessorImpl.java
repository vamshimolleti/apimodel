package com.apigee.apimodel.processor.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.apigee.apimodel.API;
import com.apigee.apimodel.Operation;
import com.apigee.apimodel.Parameter;
import com.apigee.apimodel.Resource;
import com.apigee.apimodel.ValidValue;
import com.apigee.apimodel.processor.APIProcessor;
import com.apigee.apimodel.processor.ClassificationResult;
import com.apigee.apimodel.processor.ClassificationResult.Status;

public class APIProcessorImpl
  implements APIProcessor
{
  private static final Pattern xmlPattern =
    Pattern.compile("^(text|application)/([^/+]+\\+)?xml(;.*)?");
  private static final Pattern jsonPattern =
    Pattern.compile("^(text|application)/([^/+]+\\+)?json(;.*)?");
  private static final Pattern textPattern =
    Pattern.compile("^text/plain(;.*)?");
  private static final Pattern formPattern =
    Pattern.compile("^application/x-www-form-urlencoded(;.*)?");
  private static final Pattern acceptPattern =
    Pattern.compile(".*;[\\s]*q=([0-9]+.?[0-9]*).*");
    
  private final HashMap<String, PathEntry> validPaths = new HashMap<String, PathEntry>();
  
  private static final ClassificationResult INVALID_PATH = 
    new ClassificationResultImpl(ClassificationResult.Status.INVALID_PATH);
  
  private static final Logger log = Logger.getLogger("apimodel");
  
  public APIProcessorImpl(Collection<API> apis)
  {
    for (API a : apis) {
      a.normalize();
      buildPathList(a);
    }
  }
  
  public boolean attachContext(String path, Object context)
  {
    PathEntry e = validPaths.get(path);
    if (e == null) {
      return false;
    }
    e.setContext(context);
    return true;
  }
  

  
  public ClassificationResult classifyRequest(String method, String request)
  {
    int qPos = request.indexOf('?');
    String path;
    String queryParams;
    
    if (qPos < 0) {
      path = request;
      queryParams = null;
    } else {
      path = request.substring(0, qPos);
      queryParams = request.substring(qPos + 1);
    }
    
    return classifyRequest(method, path, queryParams);
  }
  
  public ClassificationResult classifyRequest(String method, String uriPath, String queryParams)
  {
    if (log.isLoggable(Level.FINE)) {
      log.fine("Classifying " + method + ' ' + uriPath + '?' + queryParams);
    }
    
    while (!uriPath.isEmpty() && (uriPath.charAt(0) == '/')) {
      uriPath = uriPath.substring(1);
    }
    while (!uriPath.isEmpty() && (uriPath.charAt(uriPath.length() - 1) == '/')) {
      uriPath = uriPath.substring(0, uriPath.length() - 1);
    }
    
    PathEntry matched = null;
    HashMap<String, String> templateParams = new HashMap<String, String>();
    for (PathEntry entry : validPaths.values()) {
      if (entry.match(uriPath, templateParams)) {
        matched = entry;
        break;
      }
    }
    if (matched == null) {
      if (log.isLoggable(Level.FINE)) {
        log.fine("Returning " + ClassificationResult.Status.INVALID_PATH);
      }
      return INVALID_PATH;
    }
    
    Status status = Status.SUCCESSFUL;
    ClassificationResultImpl ret = new ClassificationResultImpl(status);
    for (Parameter p : matched.getResource().getTemplateParameters()) {
      String value = templateParams.get(p.getName());
      assert value != null;
      if (!isValueValid(value, p)) {
        status = Status.INVALID_TP_VALUE;
        ret.addInvalidParameter(p.getName());
      }
    }

    if (status == Status.SUCCESSFUL) {
      if (method.equalsIgnoreCase("OPTIONS")) {
        status = Status.SUCCESSFUL;
      } else {
        for (Operation o : matched.getResource().getOperations()) {
          if (method.equalsIgnoreCase(o.getMethod())) {
            return classifyOperation(matched, o, queryParams, templateParams);
          }
        }
        status = Status.INVALID_METHOD;
      }
    }
    
    ret.setStatus(status);
    buildMethodList(matched.getResource(), ret);
    ret.setResource(matched.getResource());
    ret.setProcessor(this);
    if (log.isLoggable(Level.FINE)) {
      log.fine("Returning " + status);
    }
    return ret;
  }
  
  private ClassificationResult classifyOperation(PathEntry matched, Operation o, String qps, Map<String, String> templateParams)
  {
    ClassificationResultImpl ret = new ClassificationResultImpl();
    ret.setProcessor(this);
    ret.setResource(matched.getResource());
    ret.setOperation(o);
    ret.setContext(matched.getContext());
    
    Map<String, List<String>> queryParams = splitParams(qps);
    if (log.isLoggable(Level.FINEST)) {
      log.finest("Query params: " + queryParams);
      log.finest("Template params: " + templateParams);
    }
    ret.setQueryParams(queryParams);
    ret.setTemplateParams(templateParams);
    
    HashSet<String> extraQps = new HashSet<String>(queryParams.keySet());
    for (Map.Entry<String, Parameter> reqQp : o.getQueryParameters().entrySet()) {
      if (log.isLoggable(Level.FINEST)) {
        log.finest(" Considering " + reqQp.getKey() + " required = " + reqQp.getValue().isRequired() +
                   " found = " + queryParams.containsKey(reqQp.getKey()));
      }
      List<String> queryParam = queryParams.get(reqQp.getKey());
      extraQps.remove(reqQp.getKey());
      if ((queryParam == null) || queryParam.isEmpty()) {
        if (reqQp.getValue().isRequired()) {
          ret.addMissingQueryParameter(reqQp.getKey());
        }
      } else {
        if (!reqQp.getValue().isRepeating() && (queryParam.size() > 1)) {
          if (log.isLoggable(Level.FINE)) {
            log.fine("QP " + reqQp.getKey() + " not repeating but has " + queryParam.size() + " values");
          }
          ret.addInvalidParameter(reqQp.getKey());
        } else {
          for (String v : queryParam) {
            if (!isValueValid(v, reqQp.getValue())) {
              ret.addInvalidParameter(reqQp.getKey());
            }
          }
        } 
      }
    }
    
    if (!extraQps.isEmpty()) {
      ret.setExtraQueryParams(extraQps);
    }

    // TODO fill in matrix parameters and check for missing
    
    if (log.isLoggable(Level.FINE)) {
      log.fine("  Missing query params empty = " + ret.getMissingQueryParams().isEmpty() + ": " + ret.getMissingQueryParams());
      log.fine("  Extra query params: empty = " + ret.getExtraQueryParams().isEmpty() + ": " + ret.getExtraQueryParams());
    }
    
    if (!ret.getMissingQueryParams().isEmpty()) {
      ret.setStatus(Status.REQUIRED_QPS_MISSING);
    } else if (!ret.getInvalidParameters().isEmpty()) {
      ret.setStatus(Status.INVALID_QP_VALUE);
    } else if (!ret.getExtraQueryParams().isEmpty()) {
      ret.setStatus(Status.EXTRA_QPS);
    } else {
      ret.setStatus(Status.SUCCESSFUL);
    }
    if (log.isLoggable(Level.FINE)) {
      log.fine("Returning " + ret.getStatus());
    }
    return ret;
  }
  
  private Map<String, List<String>> splitParams(String qps)
  {
    if ((qps == null) || qps.isEmpty()) {
      return Collections.EMPTY_MAP;
    }
    
    HashMap<String, List<String>> ret = new HashMap<String, List<String>>();
    String[] parts = qps.split("&");
    for (String part : parts) {
      if (part.trim().isEmpty()) {
        continue;
      }
      String[] kv = part.split("=");
      try {
        String key = URLDecoder.decode(kv[0], "UTF-8");
        String value = (kv.length > 1) ? URLDecoder.decode(kv[1], "UTF-8") : null;
        if (ret.containsKey(key)) {
          ret.get(key).add(value);
        } else {
          ArrayList<String> l = new ArrayList<String>();
          l.add(value);
          ret.put(key, l);
        }
      } catch (UnsupportedEncodingException e) {
        throw new AssertionError("This won't happen");
      }
    }
    return ret;
  }
  
  private void buildPathList(API a)
  {
    for (Resource r : a.getResources()) {
      PathEntry e = new PathEntry(r);
      validPaths.put(r.getPath(), e);
    }
  }
  
  private void buildMethodList(Resource res, ClassificationResultImpl result)
  {
    for (Operation o : res.getOperations()) {
      result.addSupportedMethod(o.getMethod());
    }
  }
  
  private boolean isValueValid(String value, Parameter p)
  {
    // TODO more types!
    if (p.getDataType().equals("int") || p.getDataType().equals("xsd:int") ||
        p.getDataType().equals("integer") || p.getDataType().equals("integer")) {
      try {
        Integer.parseInt(value);
      } catch (NumberFormatException nfe) {
        if (log.isLoggable(Level.FINE)) {
          log.fine("QP value " + value + " does not match type " + p.getDataType());
        }
        return false;
      }
    }
    if (!p.getValidValues().isEmpty()) {
      for (ValidValue vv : p.getValidValues()) {
        if (value.equals(vv.getValue())) {
          return true;
        }
      }
      if (log.isLoggable(Level.FINE)) {
        log.fine("QP value " + value + " doesn't match valid values " + p.getValidValues());
      }
      return false;
    }
    return true;
  }

  public ContentType parseContentType(String contentType)
  {
    if (contentType == null) {
      return ContentType.UNKNOWN;
    }
    if (jsonPattern.matcher(contentType).matches()) {
     return ContentType.JSON;
    }
    if (xmlPattern.matcher(contentType).matches()) {
      return ContentType.XML;
    }
    if (formPattern.matcher(contentType).matches()) {
      return ContentType.FORM;
    }
    if (textPattern.matcher(contentType).matches()) {
      return ContentType.TEXT;
    }
    return ContentType.UNKNOWN;
  }

  public ContentType parseAcceptHeader(String accept)
  {
    if (accept == null) {
      return ContentType.UNKNOWN;
    }
    String[] vals = accept.split(",");
    Arrays.sort(vals, new AcceptHeaderComparator());
    
    // TODO handle all those pesky asterisks too!
    for (String val : vals) {
      ContentType t = parseContentType(val.trim());
      if (t != ContentType.UNKNOWN) {
        return t;
      }
    }
    return ContentType.UNKNOWN;
  }
  
  private static final class AcceptHeaderComparator
    implements Comparator<String>
  {
    public int compare(String s1, String s2)
    {
      float qVal1 = extractQ(s1);
      float qVal2 = extractQ(s2);
      if (qVal1 > qVal2) {
        return -1;
      } else if (qVal1 < qVal2) {
        return 1;
      }
      return 0;
    }
    
    private float extractQ(String s)
    {
      Matcher m = acceptPattern.matcher(s);
      if (m.matches() && (m.groupCount() > 0)) {
        return Float.parseFloat(m.group(1));
      }
      return Float.MAX_VALUE;
    }
  }
}
