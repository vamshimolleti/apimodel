package com.apigee.apimodel;

import java.util.Collection;

/**
 * An organization contains a collection of APIs.
 */

public interface Organization
{
  String getName();
  void setName(String name);
  
  Collection<API> getApis();
  void addApi(API a);
  API deleteApi(String name);
}
