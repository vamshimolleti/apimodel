package com.apigee.apimodel;

public interface Representation
  extends Documented
{
  String getType();
  void setType(String type);
  
  String getMediaType();
  void setMediaType(String type);
  
  String getDefinition();
  void setDefinition(String definition);
  
  String getLink();
  void setLink(String link);
}
