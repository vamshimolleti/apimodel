package com.apigee.apimodel.servlet.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServletIT
{
  public static final String BASE = "http://localhost:8080";
  
  private HttpURLConnection open(String uri)
    throws IOException
  {
    return (HttpURLConnection)(new URL(BASE + uri).openConnection());
  }
  
  private void consumeInput(HttpURLConnection c)
    throws IOException
  {
    InputStream in = c.getInputStream();
    byte[] tmp = new byte[8192];
    int r;
    do {
      r = in.read(tmp);
    } while (r >= 0);
  }
  
  @Test
  public void testHelloWorld()
    throws IOException
  {
    HttpURLConnection conn = open("/tests");
    assertEquals(200, conn.getResponseCode());
    consumeInput(conn);
  }
  
  /* Don't know what's broken -- in the Java client somewhere.. 
  @Test
  public void testHelloWorldCORS()
    throws IOException
  {
    HttpURLConnection conn = open("/tests");
    conn.setRequestProperty("origin", "localhost");
    conn.connect();
    assertEquals(200, conn.getResponseCode());
    consumeInput(conn);
    assertEquals("localhost", conn.getHeaderField("Access-Control-Allow-Origin"));
  }
  */
  
  @Test
  public void test404()
    throws IOException
  {
    HttpURLConnection conn = open("/NOTtests");
    assertEquals(404, conn.getResponseCode());
  }
  
  @Test
  public void testMissingHandler()
    throws IOException
  {
    HttpURLConnection conn = open("/noHandler");
    assertEquals(501, conn.getResponseCode());
  }
  
  @Test
  public void testJsonGet()
    throws IOException
  {
    HttpURLConnection conn = open("/tests/object");
    assertEquals(200, conn.getResponseCode());
    assertTrue(conn.getContentType().startsWith("application/json"));
    
    ObjectMapper mapper = new ObjectMapper();
    TestObject to = mapper.readValue(conn.getInputStream(), TestObject.class);
  }
  
  @Test
  public void testJsonPost()
    throws IOException
  {
    HttpURLConnection conn = open("/tests/object");
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestMethod("POST");
    conn.setDoOutput(true);
    OutputStream out = conn.getOutputStream();
    
    TestObject to = new TestObject();
    to.setName("test");
    to.setValue(456);
    
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, to);
    out.close();
    
    assertEquals(200, conn.getResponseCode());
    TestObject to2 = mapper.readValue(conn.getInputStream(), TestObject.class);
    assertEquals(to.getName(), to2.getName());
    assertEquals(to.getValue(), to2.getValue());
  }
  
  /*
  @Test
  public void testOptionsCORSFail()
  throws IOException
  {
    HttpURLConnection conn = open("/tests/object");
    conn.setRequestMethod("OPTIONS");
    assertEquals(200, conn.getResponseCode());
    consumeInput(conn);
    assertTrue(conn.getHeaderField("Access-Control-Allow-Origin") == null);
    assertTrue(conn.getHeaderField("Access-Control-Allow-Credentials") == null);
  }
  
  private boolean containsMethod(String hdr, String m)
  {
    if (hdr == null) {
      return false;
    }
    String[] hdrs = hdr.split(",");
    for (String h : hdrs) {
      if (h.trim().equals(m)) {
        return true;
      }
    }
    return false;
  }
  
  @Test
  public void testOptionsCORSSuccess()
  throws IOException
  {
    final String Origin = "http://localhost";
    HttpURLConnection conn = open("/tests/object");
    conn.setRequestProperty("Origin", Origin);
    conn.setRequestProperty("Access-Control-Request-Method", "POST");
    conn.setRequestMethod("OPTIONS");
    assertEquals(200, conn.getResponseCode());
    consumeInput(conn);
    assertEquals(Origin, conn.getHeaderField("Access-Control-Allow-Origin"));
    assertEquals("true", conn.getHeaderField("Access-Control-Allow-Credentials"));
    assertTrue(containsMethod(conn.getHeaderField("Access-Control-Allow-Methods"), "POST"));
  }
  
  @Test
  public void testOptionsCORSFail2()
  throws IOException
  {
    final String Origin = "http://localhost";
    HttpURLConnection conn = open("/tests/object");
    conn.setRequestProperty("Origin", Origin);
    conn.setRequestProperty("Access-Control-Request-Method", "PATCH");
    conn.setRequestMethod("OPTIONS");
    assertEquals(200, conn.getResponseCode());
    consumeInput(conn);
    assertFalse(containsMethod(conn.getHeaderField("Access-Control-Allow-Methods"), "POST"));
  }
  */
}
