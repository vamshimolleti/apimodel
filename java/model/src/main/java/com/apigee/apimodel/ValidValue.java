package com.apigee.apimodel;

public interface ValidValue
  extends Documented
{
  String getValue();
  void setValue(String param);
  
  String getMediaType();
  void setMediaType(String type);
}
