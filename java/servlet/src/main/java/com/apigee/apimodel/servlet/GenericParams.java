package com.apigee.apimodel.servlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericParams
{
  protected Map<String, String> headers = Collections.EMPTY_MAP;
  protected String contentType;
  protected int contentLength = -1;

  public Map<String, String> getHeaders()
  {
    return headers;
  }
  public void setHeaders(Map<String, String> headers)
  {
    this.headers = headers;
  }
  public void setHeader(String name, String value)
  {
    if (headers == Collections.EMPTY_MAP) {
      headers = new HashMap<String, String>();
    }
    headers.put(name, value);
  }
  public String getHeader(String name) 
  {
    return headers.get(name);
  }
  public String getContentType()
  {
    return contentType;
  }
  public void setContentType(String contentType)
  {
    this.contentType = contentType;
  }
  
  public int getContentLength()
  {
    return contentLength;
  }
  public void setContentLength(int contentLength)
  {
    this.contentLength = contentLength;
  }
}
