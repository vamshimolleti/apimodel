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
import com.apigee.apimodel.processor.APIProcessor;
import com.apigee.apimodel.processor.APIProcessorFactory;
import com.apigee.apimodel.processor.ClassificationResult;
import com.apigee.apimodel.wadl.WADLReader;

public class TestClassification
{
  private static List<API> testApis;
  private APIProcessor processor;
  
  @BeforeClass
  public static void loadModel()
    throws APIModelException, IOException
  {
    testApis = WADLReader.get().readModel("twitter", TestClassification.class.getResourceAsStream("/twitter-wadl.xml"));
    
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
  public void testRoot1()
  {
    ClassificationResult r = processor.classifyRequest("GET", "/");
    assertEquals(ClassificationResult.Status.INVALID_PATH, r.getStatus());
  }
  
  @Test
  public void testRoot2()
  {
    ClassificationResult r = processor.classifyRequest("GET", "");
    assertEquals(ClassificationResult.Status.INVALID_PATH, r.getStatus());
  }
  
  @Test
  public void testNotFound1()
  {
    ClassificationResult r = processor.classifyRequest("GET", "/foo");
    assertEquals(ClassificationResult.Status.INVALID_PATH, r.getStatus());
  }
  
  @Test
  public void testNotFound2()
  {
    ClassificationResult r = processor.classifyRequest("GET", "/foo/bar/baz");
    assertEquals(ClassificationResult.Status.INVALID_PATH, r.getStatus());
  }
  
  @Test
  public void testNotFound3()
  {
    ClassificationResult r = processor.classifyRequest("GET", "/users/foo");
    assertEquals(ClassificationResult.Status.INVALID_PATH, r.getStatus());
  }
  
  @Test
  public void testNotFound4()
  {
    ClassificationResult r = processor.classifyRequest("GET", "/direct_messages/sent");
    assertEquals(ClassificationResult.Status.INVALID_PATH, r.getStatus());
  }
  
  @Test
  public void testWrongMethod1()
  {
    ClassificationResult r = processor.classifyRequest("PUT", "/statuses/home_timeline.xml");
    assertEquals(ClassificationResult.Status.INVALID_METHOD, r.getStatus());
    assertEquals(1, r.getSupportedMethods().size());
    assertTrue(r.getSupportedMethods().contains("GET"));
  }
  
  @Test
  public void testWrongMethod2()
  {
    ClassificationResult r = processor.classifyRequest("PUT", "/statuses/foo/retweeted_by.json");
    assertEquals(ClassificationResult.Status.INVALID_METHOD, r.getStatus());
    assertEquals(1, r.getSupportedMethods().size());
    assertTrue(r.getSupportedMethods().contains("GET"));
  }
  
  @Test
  public void testOptions()
  {
    ClassificationResult r = processor.classifyRequest("OPTIONS", "/statuses/foo/retweeted_by.json");
    assertEquals(ClassificationResult.Status.SUCCESSFUL, r.getStatus());
    assertEquals(1, r.getSupportedMethods().size());
    assertTrue(r.getSupportedMethods().contains("GET"));
  }
  
  @Test
  public void testMissingQuery1()
  {
    ClassificationResult r = processor.classifyRequest("GET", "/statuses/foo/retweeted_by.json");
    assertEquals(ClassificationResult.Status.REQUIRED_QPS_MISSING, r.getStatus());
    assertEquals(1, r.getMissingQueryParams().size());
    assertTrue(r.getMissingQueryParams().contains("id"));
  }
  
  @Test
  public void testMissingQuery2()
  {
    ClassificationResult r = processor.classifyRequest("GET", "/statuses/foo/retweeted_by.json?count=1&page=2");
    assertEquals(ClassificationResult.Status.REQUIRED_QPS_MISSING, r.getStatus());
    assertEquals(1, r.getMissingQueryParams().size());
    assertTrue(r.getMissingQueryParams().contains("id"));
  }
  
  @Test
  public void testRepeatedQuery()
  {
    ClassificationResult r = processor.classifyRequest("GET", "/statuses/foo/retweeted_by.json?id=123&count=1&page=2&count=2");
    assertEquals(ClassificationResult.Status.INVALID_QP_VALUE, r.getStatus());
    assertEquals(1, r.getInvalidParameters().size());
    assertTrue(r.getInvalidParameters().contains("count"));
  }
  
  @Test
  public void testTemplate1()
  {
    ClassificationResult r = processor.classifyRequest("GET", "/statuses/home_timeline.xml");
    assertEquals(ClassificationResult.Status.SUCCESSFUL, r.getStatus());
    assertEquals("xml", r.getTemplateParams().get("format"));
    assertNotNull(r.getProcessor());
  }
  
  @Test
  public void testTemplate2()
  {
    ClassificationResult r = processor.classifyRequest("GET", "/statuses/foo/retweeted_by.json?count=1&page=2");
    assertEquals(ClassificationResult.Status.REQUIRED_QPS_MISSING, r.getStatus());
    assertEquals("foo", r.getTemplateParams().get("id"));
    assertEquals("json", r.getTemplateParams().get("format"));
  }
  
  @Test
  public void testValid1()
  {
    ClassificationResult r = processor.classifyRequest("GET", "users/suggestions/books.xml");
    assertEquals(ClassificationResult.Status.SUCCESSFUL, r.getStatus());
  }
  
  @Test
  public void testValid2()
  {
    ClassificationResult r = processor.classifyRequest("GET", "users/suggestions/apigee.xml");
    assertEquals(ClassificationResult.Status.INVALID_TP_VALUE, r.getStatus());
    assertEquals(1, r.getInvalidParameters().size());
    assertTrue(r.getInvalidParameters().contains("slug"));
  }
  
  @Test
  public void testValid3()
  {
    ClassificationResult r = processor.classifyRequest("GET", "users/suggestions/books.html");
    assertEquals(ClassificationResult.Status.INVALID_TP_VALUE, r.getStatus());
  }
  
  @Test
  public void testExtra1()
  {
    ClassificationResult r = processor.classifyRequest("GET", "users/suggestions/books.xml?foo=bar");
    assertEquals(ClassificationResult.Status.EXTRA_QPS, r.getStatus());
    assertEquals(1, r.getExtraQueryParams().size());
    assertTrue(r.getExtraQueryParams().contains("foo"));
  }
  
  @Test
  public void testSetContext1()
  {
    processor.attachContext("statuses/{id}/retweeted_by.{format}", "Hello, World");
    ClassificationResult r = processor.classifyRequest("GET", "/statuses/foo/retweeted_by.json?count=1&page=2&id=bar");
    assertEquals(ClassificationResult.Status.SUCCESSFUL, r.getStatus());
    assertEquals("Hello, World", r.getContext());
  }
}
