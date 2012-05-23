package com.apigee.apimodel.api;

import com.apigee.apimodel.servlet.APIResourceHandler;

public abstract class GenericResource
  extends APIResourceHandler
{
  public Class<?> getRequestClass() {
    return null;
  }
  
  public String getPersistenceUnit() {
    return "APIModel";
  }
}
