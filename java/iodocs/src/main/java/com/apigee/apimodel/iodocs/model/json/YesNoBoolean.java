package com.apigee.apimodel.iodocs.model.json;

public enum YesNoBoolean
{
  YES("Y"), NO("N");

  private String value;

  YesNoBoolean(String value)
  {
    this.value = value;
  }

  public String toString()
  {
    return value;
  }
}
