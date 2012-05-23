package com.apigee.apimodel.iodocs.model;

import java.util.List;

public interface IOParameter
{
  String getName();
  void setName(String name);

  boolean isRequired();
  void setRequired(boolean required);

  String getDefault();
  void setDefault(String aDefault);

  String getType();
  void setType(String type);

  List<String> getEnumeratedList();
  void addEnumeratedValue(String value);

  String getDescription();
  void setDescription(String description);
}
