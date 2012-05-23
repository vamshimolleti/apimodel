package com.apigee.apimodel.servlet;

public class APIResourceException 
  extends Exception
{
  private static final long serialVersionUID = 7079627996928061980L;
  
  private int responseCode = 500;
  
  public APIResourceException(String msg)
  {
    super(msg);
  }
  
  public APIResourceException(String msg, int responseCode)
  {
    super(msg);
    this.responseCode = responseCode;
  }
  
  public APIResourceException(int responseCode)
  {
    super();
    this.responseCode = responseCode;
  }
  
  public int getResponseCode() {
    return responseCode;
  }
}
