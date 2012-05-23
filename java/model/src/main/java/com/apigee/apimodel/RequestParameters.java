package com.apigee.apimodel;

import java.util.Collection;
import java.util.Map;

public interface RequestParameters
{
  boolean isHasRequestParameters();
  
  Collection<AuthenticationMethod> getAuthenticationMethods();
  void addAuthenticationMethod(AuthenticationMethod method);
  
  Map<String, Parameter> getRequestHeaders();
  Collection<Parameter> getRequestHeaderValues();
  void addRequestHeader(Parameter hdr);
  
  Map<String, Parameter> getQueryParameters();
  Collection<Parameter> getQueryParamValues();
  void addQueryParameter(Parameter qp);
  
  String getExampleURI();
  void setExampleURI(String uri);
  
  Representation getRequestRepresentation();
  void setRequestRepresentation(Representation rep);
  
  Example getExampleRequest();
  void setExampleRequest(Example example);
  
  Collection<Tag> getTags();
  void addTag(Tag tag);
}
