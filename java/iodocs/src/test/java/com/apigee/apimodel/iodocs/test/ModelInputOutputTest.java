package com.apigee.apimodel.iodocs.test;

import com.apigee.apimodel.iodocs.model.IOEndpoint;
import com.apigee.apimodel.iodocs.model.IOMethod;
import com.apigee.apimodel.iodocs.model.IOParameter;
import com.apigee.apimodel.iodocs.model.impl.IOAPIImpl;
import com.apigee.apimodel.iodocs.model.impl.IOEndpointImpl;
import com.apigee.apimodel.iodocs.model.impl.IOMethodImpl;
import com.apigee.apimodel.iodocs.model.impl.IOParameterImpl;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class ModelInputOutputTest
{
  private ObjectMapper mapper;
  private static IOAPIImpl api;

  @BeforeClass
  public static void buildModel()
  {
    api = new IOAPIImpl();

    IOEndpoint endpoint = new IOEndpointImpl();
    endpoint.setName("Conferences");
    endpoint.setPath("/conferences");

    IOMethod method = new IOMethodImpl();
    method.setMethodName("conferences");
    method.setSynopsis("This collection method returns a list of league ids and names.");
    method.setHTTPMethod("GET");
    method.setURI("/basic/api/:parent_resource/:resource_id/conferences");
    method.setRequiresOAuth(false);

    IOParameter parameter1 = new IOParameterImpl();
    parameter1.setName("parent_resource");
    parameter1.setRequired(true);
    // parameter1.setDefault("");
    parameter1.setType("enumerated");
    parameter1.addEnumeratedValue("leagues");
    parameter1.addEnumeratedValue("foobar");
    parameter1.setDescription("Actual parent resource name");

    method.addParameter(parameter1);

    IOParameter parameter2 = new IOParameterImpl();
    parameter2.setName("resource_id");
    parameter2.setRequired(false);
    // parameter2.setDefault("");
    parameter2.setType("string");
    parameter2.setDescription("UUID of desired resource");

    method.addParameter(parameter2);

    endpoint.addMethod(method);

    api.addEndpoint(endpoint);
  }

  @Before
  public void init()
  {
    mapper = new ObjectMapper();
    mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
  }

  @Test
  public void APIOutput()
    throws IOException
  {
    System.err.println("JSON of an API:");
    System.err.println(mapper.writeValueAsString(api));
  }

}
