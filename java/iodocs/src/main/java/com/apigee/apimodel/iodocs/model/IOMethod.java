package com.apigee.apimodel.iodocs.model;

import java.util.List;

public interface IOMethod
{
  String getMethodName();
  void setMethodName(String methodName);

  String getSynopsis();
  void setSynopsis(String synopsis);

  String getHTTPMethod();
  void setHTTPMethod(String HTTPMethod);

  String getURI();
  void setURI(String URI);

  boolean isRequiresOAuth();
  void setRequiresOAuth(boolean requiresOAuth);

  List<IOParameter> getParameters();
  void addParameter(IOParameter parameter);
}
