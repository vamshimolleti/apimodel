package com.apigee.apimodel;

import java.util.Collection;
import java.util.Map;

public interface ResponseParameters
{
  boolean isHasResponseParameters();
  
  Map<String, Parameter> getResponseHeaders();
  Collection<Parameter> getResponseHeaderValues();
  void addResponseHeader(Parameter hdr);
  
  Map<Integer, ResponseCode> getResponseCodes();
  Collection<ResponseCode> getResponseCodeValues();
  void addResponseCode(ResponseCode code);
  
  Representation getResponseRepresentation();
  void setResponseRepresentation(Representation rep);
  
  Example getExampleResponse();
  void setExampleResponse(Example example);
}
