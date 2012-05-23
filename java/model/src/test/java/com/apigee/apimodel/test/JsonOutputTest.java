package com.apigee.apimodel.test;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.apigee.apimodel.Resource;
import com.apigee.apimodel.impl.APIImpl;
import com.apigee.apimodel.impl.OperationImpl;
import com.apigee.apimodel.impl.ParameterImpl;
import com.apigee.apimodel.impl.ResourceImpl;

public class JsonOutputTest
{
  private static APIImpl api;
  private ObjectMapper mapper;
  
  private static ParameterImpl makeParam(String name, String type, 
                                         boolean required)
  {
    ParameterImpl ret = new ParameterImpl();
    ret.setName(name);
    ret.setDataType(type);
    ret.setRequired(required);
    return ret;
  }
  
  private static OperationImpl makeOp(String method)
  {
    OperationImpl op = new OperationImpl();
    op.setMethod(method);
    op.addQueryParameter(makeParam("one", "int", true));
    op.addQueryParameter(makeParam("two", "string", false));
    return op;
  }
  
  @BeforeClass
  public static void buildModel()
  {
    api = new APIImpl();
    api.setName("TestAPI");
    api.setDocumentation("This is a test API");
    api.setBasePath("http://api.api.pi");
    api.setOnboardingURI("http://developer.api.pi");
    api.addRequestHeader(makeParam("x-apigee-Test", "string", false));
    
    ResourceImpl res = new ResourceImpl();
    res.setPath("/foos");
    res.setDocumentation("Some foos");
    res.addOperation(makeOp("GET"));
    res.addOperation(makeOp("POST"));
    api.addResource(res);
    
    res = new ResourceImpl();
    res.setPath("/foos/{foo}");
    res.addOperation(makeOp("GET"));
    api.addResource(res);
  }
  
  @Before
  public void init()
  {
    mapper = new ObjectMapper();
    mapper.configure(Feature.INDENT_OUTPUT, true);
  }
  
  @Test
  public void testAPIOutput()
    throws IOException
  {
    System.err.println("JSON of an API:");
    System.err.println(mapper.writeValueAsString(api));
  }
  
  @Test
  public void testResourceOutput()
    throws IOException
  {
    System.err.println("JSON of the resources:");
    System.err.println(mapper.writeValueAsString(api.getResources()));
  }
  
  @Test
  public void testOperationOutput()
    throws IOException
  {
    System.err.println("JSON of an operation:");
    System.err.println(mapper.writeValueAsString(
      api.getResources().iterator().next().getOperations().iterator().next()));
  }
}
