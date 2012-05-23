package com.apigee.apimodel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The complete, abstract model of an API.
 */

public interface API
  extends Documented
{
  long getId();
  
  String getName();
  void setName(String name);
  
  String getBasePath();
  void setBasePath(String path);
  
  String getOnboardingURI();
  void setOnboardingURI(String uri);
  
  Collection<Resource> getResources();
  void addResource(Resource r);
  
  Map<String, Parameter> getRequestHeaders();
  Collection<Parameter> getRequestHeaderValues();
  void addRequestHeader(Parameter hdr);
  
  Map<String, Parameter> getResponseHeaders();
  Collection<Parameter> getResponseHeaderValues();
  void addResponseHeader(Parameter hdr);
  
  List<APIDifference> getDifferences(API api);
  
  /**
   * A "normalized" API definition has no nested resources, and all
   * parameter definitions from the API, resource, and operation level
   * are pushed down the stack so that all levels can see all the
   * parameters defined at the higher level.
   */
  void normalize();
  boolean isNormalized();
}
