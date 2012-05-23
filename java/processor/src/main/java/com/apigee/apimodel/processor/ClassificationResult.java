package com.apigee.apimodel.processor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.apigee.apimodel.Operation;
import com.apigee.apimodel.Resource;

/**
 * This class contains the results of one-shot classification of an HTTP request.
 */

public interface ClassificationResult
{
  enum Status { 
    /** The request is 100 percent ready to go */
    SUCCESSFUL,
    /** The URI path cannot be found and 404 should be returned */
    INVALID_PATH,
    /** The HTTP operation is not valid and 405 should be returned */
    INVALID_METHOD,
    /** Required query parameters are missing */
    REQUIRED_QPS_MISSING,
    /** A query parameter has an invalid value, or a non-repeating
     *  query parameter was repeated */
    INVALID_QP_VALUE,
    /** A template parameter had an invalid value */
    INVALID_TP_VALUE,
    /** Otherwise successful but there are additional query parameters in the request.
     *  The caller may choose to accept or reject this request. */
    EXTRA_QPS
  };
      
  /** Return the status of the classification */
  Status getStatus();
  
  /** 
   * Unless the result is INVALID_PATH, return a reference to the Resource
   * object from the API model that was matched. 
   */
  Resource getResource();
  
  /**
   * Unless the result was INVALID_PATH or INVALID_METHOD, return a reference
   * to the Operation object from the API model that was matched. All
   * "inheritable" headers and other parameters will be normalized into
   * this one object.
   */
  Operation getOperation();
  
  /**
   * Unless the result was INVALID_PATH or INVALID_METHOD, return a map of
   * all the query parameters in the request and their values.
   */
  Map<String, List<String>> getQueryParams();
  
  /**
   * Unless the result was INVALID_METHOD, return a map
   * of all the template parameters in the request URI and their values.
   */
  Map<String, String> getTemplateParams();
  
  /**
   * Unless the result was INVALID_METHOD, return a map of
   * all the matrix parameters in the request URI and their values.
   */
  Map<String, String> getMatrixParams();
  
  /**
   * If the result was INVALID_QP_VALUE or INVALID_TP_VALUE, list
   * all the parameters with an invalid value.
   */
  Set<String> getInvalidParameters();
  
  /**
   * If the result was EXTRA_QPS, then this returns a list of the query parameter
   * names that were not part of the API model.
   */ 
  Set<String> getExtraQueryParams();
  
  /**
   * If the result was REQUIRED_QPS_MISSING, then this returns a list of the
   * required query parameter names that were missing from the request.
   */
  Set<String> getMissingQueryParams();
  
  /**
   * If the result is INVALID_QP_VALUE, 
  
  /**
   * In the event of an OPTIONS request or an INVALID_PATH response, return the
   * HTTP methods that are supported by the target.
   */
  Set<String> getSupportedMethods();
  
  /**
   * Return the object that was attached to this resource using the 
   * "attachContext" method.
   */
  Object getContext();
  
  /**
   * Return the API processor that did this
   */
  APIProcessor getProcessor();
}
