package com.apigee.apimodel.processor;

public interface APIProcessor
{
  enum ContentType { TEXT, FORM, JSON, XML, UNKNOWN };
  
  /**
   * Given an HTTP request, classify it according to URI path, method, and
   * resolve all template, matrix, and query parameters.
   * 
   * @param method the HTTP method name (aka "verb")
   * @param the complete URI path, including query parameters, but not including protocol or host
   */
  ClassificationResult classifyRequest(String method, String uriAndParams);
  
  /**
   * Given an HTTP request, classify it according to URI path, method, and
   * resolve all template, matrix, and query parameters.
   * <p>
   * NOTE: In the event of an OPTIONS request, this method will always return a "SUCCESSFUL"
   * result, and will contain the supported methods in the response.
   * 
   * @param method the HTTP method name (aka "verb")
   * @param uriPath the URI path, not including host, protocol, or query parameters
   * @param the complete query parameters string minus the "?", or null
   */
  ClassificationResult classifyRequest(String method, String uriPath, String queryParameters);
  
  /**
   * Given a path request that matches exactly what is found in the API model,
   * attach the given object to the specified Resource object so that it may
   * be used during classification.
   */
  boolean attachContext(String path, Object context);
  
  /**
   * Parse the Content-Type header from an HTTP request and return whether the request
   * contains XML, JSON, text, or a form-encoded parameter
   */
  ContentType parseContentType(String contentType);
  
  /**
   * Parse the Accept header from an HTTP request and determine if the caller prefers
   * JSON, XML, or text, in that order.
   */
  ContentType parseAcceptHeader(String accept);
}
