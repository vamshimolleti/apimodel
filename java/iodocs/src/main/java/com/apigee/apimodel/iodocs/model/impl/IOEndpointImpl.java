package com.apigee.apimodel.iodocs.model.impl;

import com.apigee.apimodel.iodocs.model.IOEndpoint;
import com.apigee.apimodel.iodocs.model.IOMethod;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class IOEndpointImpl
  implements IOEndpoint
{
  private String name;
  private String path;

  @JsonDeserialize(contentAs = IOMethodImpl.class)
  private final List<IOMethod> methods = new ArrayList<IOMethod>();

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getPath()
  {
    return path;
  }

  public void setPath(String path)
  {
    this.path = path;
  }

  public List<IOMethod> getMethods()
  {
    return methods;
  }

  public void addMethod(IOMethod method)
  {
    methods.add(method);
  }

}
