package com.apigee.apimodel.servlet;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apigee.apimodel.APIModelException;
import com.apigee.apimodel.processor.ClassificationResult;

/**
 * This class describes a resource class object -- we create one for every resource that the
 * user maps in the model. It stores information about how to invoke the API based on reflection.
 */

public class ResourceClassInfo
{
  public static final String QUERY_PARAMS_PROP = "queryParameters";
  public static final String TEMPLATE_PARAMS_PROP = "templateParameters";
  public static final String MATRIX_PARAMS_PROP  = "matrixParameters";
  public static final String REQ_HEADERS_PROP  = "requestHeaders";
  public static final String INPUT_STREAM_PROP  = "inputStream";
  public static final String OUTPUT_STREAM_PROP  = "outputStream";
  
  public static final String REQ_CONTENT_LENGTH_PROP  = "requestContentLength";
  public static final String REQ_CONTENT_TYPE_PROP  = "requestContentType";
  
  public static final String RESP_CONTENT_LENGTH_PROP  = "responseContentLength";
  public static final String RESP_CONTENT_TYPE_PROP  = "responseContentType";
  public static final String RESP_HEADERS_PROP  = "responseHeaders";
  public static final String RESPONSE_CODE_PROP = "responseCode";
  
  public static final String GET_METHOD = "get";
  public static final String POST_METHOD = "post";
  public static final String PUT_METHOD = "put";
  public static final String DELETE_METHOD = "delete";
  public static final String TRACE_METHOD = "trace";
  public static final String HEAD_METHOD = "head";
  public static final String OPTIONS_METHOD = "options";
  public static final String PATCH_METHOD = "patch";
  
  private final Class<?> handlerClass;
  
  private Method setQueryParams;
  private Method setTemplateParams;
  private Method setMatrixParams;
  private Method setRequestHeaders;
  private Method setInputStream;
  private Method setOutputStream;
  
  private Method setRequestContentType;
  private Method setRequestContentLength;
  
  private Method getResponseContentLength;
  private Method getResponseContentType;
  private Method getResponseHeaders;
  private Method getResponseCode;
  
  private Method doGet;
  private Method doPost;
  private Method doPut;
  private Method doDelete;
  private Method doTrace;
  private Method doHead;
  private Method doOptions;
  private Method doPatch;
  
  public ResourceClassInfo(Class<?> klass)
    throws APIModelException
  {
    this.handlerClass = klass;
    
    BeanInfo info;
    try {
      info = Introspector.getBeanInfo(klass);
    } catch (IntrospectionException ie) {
      throw new APIModelException("Cannot discover methods on the resource object", ie);
    }
    
    for (PropertyDescriptor p : info.getPropertyDescriptors()) {
      if (p.getName().equals(QUERY_PARAMS_PROP) && (p.getPropertyType() == Map.class)) {
        setQueryParams = p.getWriteMethod();
      } else if (p.getName().equals(TEMPLATE_PARAMS_PROP) && (p.getPropertyType() == Map.class)) {
        setTemplateParams = p.getWriteMethod();
      } else if (p.getName().equals(MATRIX_PARAMS_PROP) && (p.getPropertyType() == Map.class)) {
        setMatrixParams = p.getWriteMethod();
      } else if (p.getName().equals(REQ_HEADERS_PROP) && (p.getPropertyType() == Map.class)) {
        setRequestHeaders = p.getWriteMethod();
      } else if (p.getName().equals(RESP_HEADERS_PROP) && (p.getPropertyType() == Map.class)) {
        getResponseHeaders = p.getReadMethod();
      } else if (p.getName().equals(INPUT_STREAM_PROP) && (p.getPropertyType() == InputStream.class)) {
        setInputStream = p.getWriteMethod();
      } else if (p.getName().equals(OUTPUT_STREAM_PROP) && (p.getPropertyType() == OutputStream.class)) {
        setOutputStream = p.getWriteMethod();
      } else if (p.getName().equals(REQ_CONTENT_LENGTH_PROP) && (p.getPropertyType() == Integer.TYPE)) {
        setRequestContentLength = p.getWriteMethod();
      } else if (p.getName().equals(RESP_CONTENT_LENGTH_PROP) && (p.getPropertyType() == Integer.TYPE)) {
        getResponseContentLength = p.getReadMethod();
      } else if (p.getName().equals(REQ_CONTENT_TYPE_PROP) && (p.getPropertyType() == String.class)) {
        setRequestContentType = p.getWriteMethod();
      } else if (p.getName().equals(RESP_CONTENT_TYPE_PROP) && (p.getPropertyType() == String.class)) {
        getResponseContentType = p.getReadMethod();
      } else if (p.getName().equals(RESPONSE_CODE_PROP) && (p.getPropertyType() == Integer.TYPE)) {
        getResponseCode = p.getReadMethod();
      }
    }
    
    for (MethodDescriptor m : info.getMethodDescriptors()) {
      if (m.getName().equals(GET_METHOD)) {
        doGet = m.getMethod();
      } else if (m.getName().equals(POST_METHOD)) {
        doPost = m.getMethod();
      } else if (m.getName().equals(PUT_METHOD)) {
        doPut = m.getMethod();
      } else if (m.getName().equals(DELETE_METHOD)) {
        doDelete = m.getMethod();
      } else if (m.getName().equals(TRACE_METHOD)) {
        doTrace = m.getMethod();
      } else if (m.getName().equals(HEAD_METHOD)) {
        doHead = m.getMethod();
      } else if (m.getName().equals(OPTIONS_METHOD)) {
        doOptions = m.getMethod();
      } else if (m.getName().equals(PATCH_METHOD)) {
        doPatch = m.getMethod();
      }
    }
  }
  
  private void setProperty(Method m, Object t, Object v, String n)
    throws APIModelException
  {
    try {
      m.invoke(t, v);
    } catch (IllegalArgumentException iae) {
      throw new APIModelException("Cannot set property " + n, iae);
    } catch (IllegalAccessException ie) {
      throw new APIModelException("Cannot set property " + n, ie);
    } catch (InvocationTargetException ite) {
      if (ite.getTargetException() != null) {
        throw new APIModelException("Cannot set property " + n, ite.getTargetException());
      }
      throw new APIModelException("Cannot set property " + n, ite);
    }
  }
  
  private Object getProperty(Method m, Object t, String n)
    throws IOException
  {
    try {
      return m.invoke(t);
    } catch (IllegalArgumentException iae) {
      throw new IOException("Cannot get property " + n, iae);
    } catch (IllegalAccessException ie) {
      throw new IOException("Cannot get property " + n, ie);
    } catch (InvocationTargetException ite) {
      if (ite.getTargetException() != null) {
        throw new IOException("Cannot get property " + n, ite.getTargetException());
      }
      throw new IOException("Cannot get property " + n, ite);
    }
  }

  public void service(HttpServletRequest req, HttpServletResponse resp, ClassificationResult classification)
    throws IOException, ServletException, APIModelException
  {    
    Object target;
    try {
      target = handlerClass.newInstance();
    } catch (IllegalAccessException ie) {
      throw new APIModelException("Cannot create target object", ie);
    } catch (InstantiationException ie) {
      throw new APIModelException("Cannot create target object", ie);
    }
    
    LazyServletOutputStream out = new LazyServletOutputStream(resp, this, target);
    
    if (setRequestContentLength != null) {
    setProperty(setRequestContentLength, target, req.getContentLength(), REQ_CONTENT_LENGTH_PROP);
    }
    if (setRequestContentType != null) {
      setProperty(setRequestContentType, target, req.getContentType(), REQ_CONTENT_TYPE_PROP);
    }
    if (setQueryParams != null) {
      setProperty(setQueryParams, target, classification.getQueryParams(), QUERY_PARAMS_PROP);
    }
    if (setTemplateParams != null) {
      setProperty(setTemplateParams, target, classification.getTemplateParams(), TEMPLATE_PARAMS_PROP);
    }
    if (setMatrixParams != null) {
      setProperty(setMatrixParams, target, classification.getMatrixParams(), MATRIX_PARAMS_PROP);
    }
    if (setInputStream != null) {
      setProperty(setInputStream, target, req.getInputStream(), INPUT_STREAM_PROP);
    }
    if (setOutputStream != null) {
      setProperty(setOutputStream, target, out, OUTPUT_STREAM_PROP);
    }
    if (setRequestHeaders != null) {
      Map<String, String> hdrs = buildRequestHeaders(req);
      setProperty(setRequestHeaders, target, hdrs, REQ_HEADERS_PROP);
    }
    
    Method toInvoke = null;
    if (classification.getOperation().getMethod().equalsIgnoreCase("DELETE")) {
      toInvoke = doDelete;
    } else if (classification.getOperation().getMethod().equalsIgnoreCase("GET")) {
      toInvoke = doGet;
    } else if (classification.getOperation().getMethod().equalsIgnoreCase("HEAD")) {
      toInvoke = doHead;
    } else if (classification.getOperation().getMethod().equalsIgnoreCase("OPTIONS")) {
      toInvoke = doOptions;
    } else if (classification.getOperation().getMethod().equalsIgnoreCase("PATCH")) {
      toInvoke = doPatch;
    } else if (classification.getOperation().getMethod().equalsIgnoreCase("POST")) {
      toInvoke = doPost;
    } else if (classification.getOperation().getMethod().equalsIgnoreCase("PUT")) {
      toInvoke = doPut;
    } else if (classification.getOperation().getMethod().equalsIgnoreCase("TRACE")) {
      toInvoke = doTrace;
    }
    
    if (toInvoke == null) {
      throw new APIModelException("No handler found");
    }
     
    try {
      toInvoke.invoke(target);
    } catch (IllegalArgumentException iae) {
      throw new APIModelException("Cannot invoke handler", iae);
    } catch (IllegalAccessException ie) {
      throw new APIModelException("Cannot invoke handler", ie);
    } catch (InvocationTargetException ite) {
      if (ite.getTargetException() != null) {
        if (ite.getTargetException() instanceof ServletException) {
          throw (ServletException)ite.getTargetException(); 
        } else if (ite.getTargetException() instanceof IOException) {
          throw (IOException)ite.getTargetException(); 
        } else {
          throw new APIModelException("Cannot invoke handler", ite.getTargetException());
        }
      }
      throw new APIModelException("Cannot invoke handler", ite);
    }
    
    out.close();
  }
  
  void updateResponse(HttpServletResponse resp, Object target)
    throws IOException
  {
    if (getResponseContentLength != null) {
      Object cl = getProperty(getResponseContentLength, target, RESP_CONTENT_LENGTH_PROP);
      if (cl != null) {
        int cli = ((Integer)cl).intValue();
        if (cli >= 0) {
          resp.setContentLength(((Integer)cl).intValue());
        }
      }
    }
    if (getResponseContentType != null) {
      Object ct = getProperty(getResponseContentType, target, RESP_CONTENT_TYPE_PROP);
      if (ct != null) {
        resp.setContentType((String)ct);
      }
    }
    if (getResponseHeaders != null) {
      Object rh = getProperty(getResponseHeaders, target, RESP_HEADERS_PROP);
      if (rh != null) {
        for (Map.Entry<String, String> h : ((Map<String, String>)rh).entrySet()) {
          resp.setHeader(h.getKey(), h.getValue());
        }
      }
    }
    if (getResponseCode != null) {
      Object rc = getProperty(getResponseCode, target, RESPONSE_CODE_PROP);
      if (rc != null) {
        int rci = ((Integer)rc).intValue();
        if (rci >= 0) {
          resp.setStatus(((Integer)rc).intValue());
        }
      }
    }
  }
  
  private Map<String, String> buildRequestHeaders(HttpServletRequest req)
  {
    HashMap<String, String> ret = new HashMap<String, String>();
    Enumeration<String> names = req.getHeaderNames();
    while (names.hasMoreElements()) {
      String hn = names.nextElement();
      ret.put(hn, req.getHeader(hn));
    }
    return ret;
  }
}
