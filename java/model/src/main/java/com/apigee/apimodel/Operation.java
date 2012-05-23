package com.apigee.apimodel;

public interface Operation
  extends RequestParameters, ResponseParameters, Documented
{
  String getDisplayName();
  void setDisplayName(String name);
  
  Resource getResource();
  void setResource(Resource resource);
  
  String getMethod();
  void setMethod(String method);
}
