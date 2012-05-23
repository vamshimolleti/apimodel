package com.apigee.apimodel;

public interface ResponseCode
  extends Documented
{
  int getCode();
  void setCode(int code);
  
  String getMessage();
  void setMessage(String message);
  
  boolean isSuccess();
  void setSuccess(boolean success);
}
