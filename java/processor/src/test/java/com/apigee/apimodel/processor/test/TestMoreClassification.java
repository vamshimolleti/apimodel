package com.apigee.apimodel.processor.test;

import java.io.IOException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIModelException;
import com.apigee.apimodel.apitext.APITextReader;
import com.apigee.apimodel.processor.APIProcessor;
import com.apigee.apimodel.processor.APIProcessorFactory;
import com.apigee.apimodel.processor.ClassificationResult;
import com.apigee.apimodel.processor.ClassificationResult.Status;

public class TestMoreClassification
{
  private static List<API> testApis;
  private APIProcessor processor;
  
  @BeforeClass
  public static void loadModel()
    throws APIModelException, IOException
  {
    testApis = APITextReader.get().readModel("apiapi", TestClassification.class.getResourceAsStream("/apiapi.md"));
    
    Logger log = Logger.getLogger("apimodel");
    log.setLevel(Level.FINEST);
    Handler h = new ConsoleHandler();
    h.setLevel(Level.FINEST);
    h.setFormatter(new SimpleFormatter());
    log.addHandler(h);
  }
  
  @Before
  public void createClassifier()
  {
    processor = APIProcessorFactory.get().createProcessor(testApis);
  }
  
  @Test
  public void testPostNew1()
  {
    ClassificationResult r = processor.classifyRequest("DELETE", "/o/foo/apis");
    assertEquals(Status.INVALID_METHOD, r.getStatus());
  }
  
  @Test
  public void testPostNew2()
  {
    ClassificationResult r = processor.classifyRequest("POST", "/o/foo/apis", "name=foo");
    assertEquals(Status.REQUIRED_QPS_MISSING, r.getStatus());
  }
  
  @Test
  public void testPostNew3()
  {
    ClassificationResult r = processor.classifyRequest("POST", "/o/foo/apis", "baz=foo&format=invalid&name=foo&foo=bar");
    assertEquals(Status.INVALID_QP_VALUE, r.getStatus());
    assertTrue(r.getInvalidParameters().contains("format"));
  }
  
  @Test
  public void testPostNew4()
  {
    ClassificationResult r = processor.classifyRequest("POST", "/o/foo/apis", "format=invalid&name=foo");
    assertEquals(Status.INVALID_QP_VALUE, r.getStatus());
    assertTrue(r.getInvalidParameters().contains("format"));
  }
  
  @Test
  public void testPostNew5()
  {
    ClassificationResult r = processor.classifyRequest("POST", "/o/foo/apis", "format=wadl&name=foo");
    assertEquals(Status.SUCCESSFUL, r.getStatus());
    assertTrue(r.getQueryParams().get("format").contains("wadl"));
    assertTrue(r.getQueryParams().get("name").contains("foo"));
  }
  
  @Test
  public void testPostNew6()
  {
    ClassificationResult r = processor.classifyRequest("POST", "/o/foo/apis", "format=wadl&name=foo&bar=baz");
    assertEquals(Status.EXTRA_QPS, r.getStatus());
    assertTrue(r.getExtraQueryParams().contains("bar"));
  }
}
