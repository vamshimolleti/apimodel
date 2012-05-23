package com.apigee.apimodel;

public class APIModelException 
  extends Exception
{
  public APIModelException(String msg)
  {
    super(msg);
  }
  
  public APIModelException(String msg, Throwable t)
  {
    super(msg, t);
  }
  
  public APIModelException(Throwable t)
  {
    super(t);
  }
}
