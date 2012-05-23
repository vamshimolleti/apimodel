package com.apigee.apimodel.iodocs.model.impl;

import com.apigee.apimodel.iodocs.model.IOAPI;
import com.apigee.apimodel.iodocs.model.IOEndpoint;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class IOAPIImpl
  implements IOAPI
{
  @JsonDeserialize(contentAs = IOEndpointImpl.class)
  private final List<IOEndpoint> endpoints = new ArrayList<IOEndpoint>();

  public List<IOEndpoint> getEndpoints()
  {
    return endpoints;
  }

  public void addEndpoint(IOEndpoint endpoint)
  {
    endpoints.add(endpoint);
  }

}
