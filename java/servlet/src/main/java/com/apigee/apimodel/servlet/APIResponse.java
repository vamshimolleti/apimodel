package com.apigee.apimodel.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apigee.apimodel.processor.APIProcessor;
import com.apigee.apimodel.processor.ClassificationResult;

public class APIResponse
  extends GenericParams
{
  private APIProcessor.ContentType requestedContentType;
  private int responseCode = -1;
  private HttpServletResponse response;
  private boolean responseCommitted;
  
  APIResponse(HttpServletRequest req, HttpServletResponse resp, ClassificationResult classification)
  {
    requestedContentType = 
      classification.getProcessor().parseAcceptHeader(req.getHeader("Accept"));
    this.response = resp;
  }
  
  void apply()
  {
    for (Map.Entry<String, String> rh : headers.entrySet()) {
      response.setHeader(rh.getKey(), rh.getValue());
    }
    if (contentType != null) {
      response.setContentType(contentType);
    }
    if (contentLength >= 0) {
      response.setContentLength(contentLength);
    }
    if (responseCode >= 0) {
      response.setStatus(responseCode);
    }
  }

  public APIProcessor.ContentType getRequestedContentType() {
    return requestedContentType;
  }

  public void setRequestedContentType(
      APIProcessor.ContentType requestedContentType)
  {
    this.requestedContentType = requestedContentType;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(int responseCode) {
    this.responseCode = responseCode;
  }
  
  public boolean isResponseCommitted() {
    return responseCommitted;
  }
  
  public OutputStream getOutputStream() 
    throws IOException
  {
    if (responseCommitted) {
      throw new IOException("Already got the output stream for the response");
    }
    apply();
    responseCommitted = true;
    return response.getOutputStream();
  }
}
