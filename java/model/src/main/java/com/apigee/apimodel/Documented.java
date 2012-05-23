package com.apigee.apimodel;

public interface Documented
{
  String getNodeId();
  void setNodeId(String id);
  
  String getDocumentation();
  void setDocumentation(String doc);
  
  String getDocumentationURI();
  void setDocumentationURI(String uri);
}
