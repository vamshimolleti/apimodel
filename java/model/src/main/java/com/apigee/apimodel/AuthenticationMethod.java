package com.apigee.apimodel;

public interface AuthenticationMethod 
  extends Documented
{
  final String UNSPECIFIED = "unspecified";
  
  String getName();
  void setName(String name);
  
  String getDescription();
  void setDescription(String name);
}
