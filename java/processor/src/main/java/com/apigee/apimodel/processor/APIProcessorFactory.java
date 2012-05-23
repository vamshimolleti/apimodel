package com.apigee.apimodel.processor;

import java.util.Collection;
import java.util.Collections;

import com.apigee.apimodel.API;
import com.apigee.apimodel.processor.impl.APIProcessorImpl;

public class APIProcessorFactory
{
  private static final APIProcessorFactory factory = new APIProcessorFactory();
  
  private APIProcessorFactory()
  {
  }
  
  public static APIProcessorFactory get() {
    return factory;
  }
  
  public APIProcessor createProcessor(Collection<API> apis)
  {
    return new APIProcessorImpl(apis);
  }
  
  public APIProcessor createProcessor(API api)
  {
    return createProcessor(Collections.singleton(api));
  }
}
