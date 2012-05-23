package com.apigee.apimodel;

import java.util.Collection;

/**
 * A generic interface for anything that is a parameter, with name, type, and requiredness.
 */

public interface Parameter
  extends Documented
{
  public enum Type {TEMPLATE, MATRIX, QUERY, REQUEST_HEADER, RESPONSE_HEADER};
  
  String getName();
  void setName(String name);
  
  Type getType();
  void setType(Type type);
  
  boolean isRepeating();
  void setRepeating(boolean repeating);
  
  String getDataType();
  void setDataType(String dataType);

  Collection<ValidValue> getValidValues();
  void addValidValue(ValidValue value);
  
  boolean isRequired();
  void setRequired(boolean required);
  
  String getDefault();
  void setDefault(String def);
}
