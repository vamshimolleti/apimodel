package com.apigee.apimodel.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.apigee.apimodel.processor.APIProcessor;
import com.apigee.apimodel.processor.ClassificationResult;

public class APIRequest
  extends GenericParams
{
  private final Map<String, String> queryParameters;
  private final APIProcessor.ContentType parsedContentType;
  private final Map<String, String> templateParameters;
  private final Map<String, String> matrixParameters;
  
  APIRequest(HttpServletRequest req, ClassificationResult classification)
  {
    Enumeration hNames = req.getHeaderNames();
    while (hNames.hasMoreElements()) {
      String hn = (String)hNames.nextElement();
      setHeader(hn, req.getHeader(hn));
    }
    contentType = req.getContentType();
    contentLength = req.getContentLength();
    parsedContentType = classification.getProcessor().parseContentType(contentType);
    
    if (classification.getQueryParams().isEmpty()) {
      queryParameters = Collections.EMPTY_MAP;
    } else {
      queryParameters = new HashMap<String, String>(classification.getQueryParams().size());
      for (Map.Entry<String, List<String>> qp : classification.getQueryParams().entrySet()) {
        queryParameters.put(qp.getKey(), qp.getValue().get(0));
      }
    }
    templateParameters = classification.getTemplateParams();
    matrixParameters = classification.getMatrixParams();
  }
  
  public APIProcessor.ContentType getParsedContentType() {
    return parsedContentType;
  }
  public Map<String, String> getTemplateParameters() {
    return templateParameters;
  }
  public String getTemplateParameter(String name) {
    return templateParameters.get(name);
  }
  public Map<String, String> getQueryParameters() {
    return queryParameters;
  }
  public String getQueryParameter(String name) {
    return queryParameters.get(name);
  }
  public Map<String, String> getMatrixParameters() {
    return matrixParameters;
  }
  public String getMatrixParameter(String name) {
    return matrixParameters.get(name);
  }
}
