package com.apigee.apimodel.processor.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.apigee.apimodel.Operation;
import com.apigee.apimodel.Resource;
import com.apigee.apimodel.processor.APIProcessor;
import com.apigee.apimodel.processor.ClassificationResult;

public class ClassificationResultImpl 
  implements ClassificationResult
{
  private APIProcessor processor;
  private Status status;
  private Resource resource;
  private Operation operation;
  private Object context;
  
  private Map<String, List<String>> queryParams = Collections.EMPTY_MAP;
  private Map<String, String> templateParams = Collections.EMPTY_MAP;
  private Map<String, String> matrixParams = Collections.EMPTY_MAP;
  
  private Set<String> invalidParams = Collections.EMPTY_SET;
  private Set<String> extraQueryParams = Collections.EMPTY_SET;
  private Set<String> missingQueryParams = Collections.EMPTY_SET;
  private Set<String> supportedMethods = Collections.EMPTY_SET;
  
  public ClassificationResultImpl()
  {
  }
  
  public ClassificationResultImpl(Status s)
  {
    this.status = s;
  }

  public Status getStatus()
  {
    return status;
  }

  public void setStatus(Status status)
  {
    this.status = status;
  }

  public Resource getResource()
  {
    return resource;
  }

  public void setResource(Resource resource)
  {
    this.resource = resource;
  }

  public Operation getOperation()
  {
    return operation;
  }

  public void setOperation(Operation operation)
  {
    this.operation = operation;
  }
  
  public Object getContext()
  {
    return context;
  }
  
  void setContext(Object context)
  {
    this.context = context;
  }

  public Map<String, List<String>> getQueryParams()
  {
    return Collections.unmodifiableMap(queryParams);
  }
  
  public void setQueryParams(Map<String, List<String>> qps) 
  {
    this.queryParams = qps;
  }

  public Map<String, String> getTemplateParams()
  {
    return Collections.unmodifiableMap(templateParams);
  }
  
  void addTemplateParam(String key, String value)
  {
    if (templateParams == Collections.EMPTY_MAP) {
      templateParams = new HashMap<String, String>();
    }
    this.templateParams.put(key, value);
  }
  
  void setTemplateParams(Map<String, String> p) 
  {
    this.templateParams = p;
  }

  public Map<String, String> getMatrixParams()
  {
    return Collections.unmodifiableMap(matrixParams);
  }

  public Set<String> getExtraQueryParams()
  {
    return extraQueryParams;
  }
  
  public void setExtraQueryParams(Set<String> qps)
  {
    this.extraQueryParams = qps;
  }

  public Set<String> getMissingQueryParams()
  {
    return missingQueryParams;
  }
  
  public void addMissingQueryParameter(String qp)
  {
    if (missingQueryParams == Collections.EMPTY_SET) {
      missingQueryParams = new HashSet<String>();
    }
    missingQueryParams.add(qp);
  }
  
  public Set<String> getInvalidParameters()
  {
    return invalidParams;
  }
  
  public void addInvalidParameter(String p)
  {
    if (invalidParams == Collections.EMPTY_SET) {
      invalidParams = new HashSet<String>();
    }
    invalidParams.add(p);
  }
  
  public Set<String> getSupportedMethods() 
  {
    return supportedMethods;
  }
  
  public void addSupportedMethod(String method)
  {
    if (supportedMethods == Collections.EMPTY_SET) {
      supportedMethods = new HashSet<String>();
    }
    supportedMethods.add(method);
  }

  public APIProcessor getProcessor()
  {
    return processor;
  }

  public void setProcessor(APIProcessor processor)
  {
    this.processor = processor;
  }
}
