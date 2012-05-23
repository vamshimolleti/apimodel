package com.apigee.apimodel.iodocs.model;

import java.util.List;

public interface IOEndpoint
{
  String getName();
  void setName(String name);

  String getPath();
  void setPath(String path);

  List<IOMethod> getMethods();
  void addMethod(IOMethod method);
}
