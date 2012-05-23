package com.apigee.apimodel;

public interface Tag
{
  final String CATEGORY_TYPE = "CATEGORY";
  final String PRIMARY_CATEGORY_TYPE = "PRIMARY_CATEGORY";
  
  String getType();
  void setType(String type);
  
  String getValue();
  void setValue(String value);
}
