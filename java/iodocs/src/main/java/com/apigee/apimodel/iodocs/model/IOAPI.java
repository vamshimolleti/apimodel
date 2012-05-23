package com.apigee.apimodel.iodocs.model;

import java.util.List;

public interface IOAPI
{
  List<IOEndpoint> getEndpoints();
  void addEndpoint(IOEndpoint endpoint);
}
