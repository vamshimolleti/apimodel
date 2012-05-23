package com.apigee.apimodel;

public interface Example 
  extends Documented
{
  String getMediaType();
  void setMediaType(String type);
  
  String getContents();
  void setContents(String contents);
  
  String getUri();
  void setUri(String uri);
}
