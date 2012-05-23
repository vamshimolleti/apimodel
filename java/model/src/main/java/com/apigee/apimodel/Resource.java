package com.apigee.apimodel;

import java.util.Collection;
import java.util.List;

/**
 * A Resource located at a specific URI. Resources have one Invocation object for each HTTP method.
 */

public interface Resource
  extends RequestParameters, ResponseParameters, Documented
{
  API getParentAPI();
  void setParentAPI(API a);
  
  Resource getParentResource();
  void setParentResource(Resource res);
  
  Collection<Resource> getChildResources();
  void addChildResource(Resource res);
  
  String getPath();
  void setPath(String path);
  
  List<Parameter> getTemplateParameters();
  void addTemplateParameter(Parameter parameter);
  
  List<Parameter> getMatrixParameters();
  void addMatrixParameter(Parameter parameter);
  
  Collection<Operation> getOperations();
  void addOperation(Operation op);
}
