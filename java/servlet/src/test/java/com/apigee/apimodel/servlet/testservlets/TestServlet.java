package com.apigee.apimodel.servlet.testservlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet 
  extends HttpServlet
{
  private boolean initialized;
  
  public void init(ServletConfig cfg)
    throws ServletException
  {
    super.init(cfg);
    initialized = true;
  }
  
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException, ServletException
  {
    if (!initialized) {
      throw new ServletException("init was never called");
    }
    resp.setContentType("text/plain");
    PrintWriter w = resp.getWriter();
    w.print("Hello, World!");
    w.close();
  }
}
