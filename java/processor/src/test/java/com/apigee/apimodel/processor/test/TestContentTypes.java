package com.apigee.apimodel.processor.test;
import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIModelException;
import com.apigee.apimodel.processor.APIProcessor;
import com.apigee.apimodel.processor.APIProcessor.ContentType;
import com.apigee.apimodel.processor.APIProcessorFactory;
import com.apigee.apimodel.wadl.WADLReader;

import static org.junit.Assert.*;

public class TestContentTypes
{
  private static APIProcessor p;
  
  @BeforeClass
  public static void init()
    throws APIModelException, IOException
  {
    List<API> testApis = 
        WADLReader.get().readModel("twitter", TestClassification.class.getResourceAsStream("/twitter-wadl.xml"));
    p = APIProcessorFactory.get().createProcessor(testApis);
  }
  
  @Test
  public void testJson1()
  {
    assertEquals(ContentType.JSON, p.parseContentType("application/json"));
  }
  
  @Test
  public void testJson2()
  {
    assertEquals(ContentType.JSON, p.parseContentType("application/json;charset=UTF-8"));
  }
  
  @Test
  public void testJson3()
  {
    assertEquals(ContentType.JSON, p.parseContentType("application/foobar+json"));
  }
  
  @Test
  public void testJson4()
  {
    assertEquals(ContentType.JSON, p.parseContentType("application/foobar+json;charset=ascii ;foo=bar"));
  }
  
  @Test
  public void testXml1()
  {
    assertEquals(ContentType.XML, p.parseContentType("application/xml"));
  }
  
  @Test
  public void testXml2()
  {
    assertEquals(ContentType.XML, p.parseContentType("application/xml;charset=UTF-8"));
  }
  
  @Test
  public void testXml3()
  {
    assertEquals(ContentType.XML, p.parseContentType("application/foobar+xml"));
  }
  
  @Test
  public void testXml4()
  {
    assertEquals(ContentType.XML, p.parseContentType("application/foobar+xml;charset=ascii ;foo=bar"));
  }
  
  @Test
  public void testXml5()
  {
    assertEquals(ContentType.XML, p.parseContentType("text/xml;charset=nothing"));
  }
  
  @Test
  public void testText()
  {
    assertEquals(ContentType.TEXT, p.parseContentType("text/plain;charset=utf-8"));
  }
  
  @Test
  public void testForm()
  {
    assertEquals(ContentType.FORM, p.parseContentType("application/x-www-form-urlencoded"));
  }
  
  @Test
  public void testAcceptJson1()
  {
    assertEquals(ContentType.JSON, p.parseAcceptHeader("application/json"));
  }
  
  @Test
  public void testAcceptJson2()
  {
    assertEquals(ContentType.JSON, p.parseAcceptHeader("application/json,application/xml"));
  }
  
  @Test
  public void testAcceptJson4()
  {
    assertEquals(ContentType.JSON, p.parseAcceptHeader("application/xml;q=0.8, application/json;q=0.9"));
  }
  
  @Test
  public void testAcceptXml1()
  {
    assertEquals(ContentType.XML, p.parseAcceptHeader("application/xml"));
  }
  
  @Test
  public void testAcceptXml2()
  {
    assertEquals(ContentType.XML, p.parseAcceptHeader("text/xml;q=0.9, application/json;q=0.5"));
  }
  
  @Test
  public void testAcceptXml3()
  {
    assertEquals(ContentType.XML, p.parseAcceptHeader("application/json;q=1, text/xml;q=4"));
  }
  
  @Test
  public void testNull1()
  {
    assertEquals(ContentType.UNKNOWN, p.parseContentType(null));
  }
  
  @Test
  public void testNull2()
  {
    assertEquals(ContentType.UNKNOWN, p.parseAcceptHeader(null));
  }
}
