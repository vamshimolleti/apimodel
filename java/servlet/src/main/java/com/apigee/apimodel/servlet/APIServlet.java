package com.apigee.apimodel.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIModelException;
import com.apigee.apimodel.apitext.APITextReader;
import com.apigee.apimodel.processor.APIProcessor;
import com.apigee.apimodel.processor.APIProcessorFactory;
import com.apigee.apimodel.processor.ClassificationResult;
import com.apigee.apimodel.wadl.WADLReader;

public class APIServlet
  extends HttpServlet
{
  enum Cors { NONE, AUTHENTICATED, ON };
  
  private APIProcessor processor;
  private boolean verboseErrors;
  private Cors supportCors = Cors.NONE;
  
  public void init(ServletConfig cfg)
    throws ServletException
  {
    super.init(cfg);
    String model = cfg.getInitParameter("APIModel");
    String mapping = cfg.getInitParameter("APIMapping");
    String verboseString = cfg.getInitParameter("VerboseErrors");
    String corsString = cfg.getInitParameter("SupportCORS");
    
    if ((model == null) || (mapping == null)) {
      throw new ServletException("Both APIModel and APIMapping init parameters must be set");
    }
    
    try {
      buildAPIProcessor(model, mapping, cfg);
    } catch (IOException ioe) {
      throw new ServletException(ioe);
    } catch (APIModelException ame) {
      throw new ServletException(ame);
    }
    
    if (verboseString != null) {
      verboseErrors = Boolean.valueOf(verboseString);
    }
    if (corsString != null) {
      if (corsString.equalsIgnoreCase("authenticated")) {
        supportCors = Cors.AUTHENTICATED;
      } else if (corsString.equalsIgnoreCase("true")) {
        supportCors = Cors.ON;
      }
    }
  }
  
  private void sendError(HttpServletResponse resp, int status, String msg)
    throws IOException
  {
    resp.setStatus(status);
    if (verboseErrors) {
      resp.setContentType("text/plain");
      OutputStream out = resp.getOutputStream();
      PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
      pw.println(msg);
      pw.close();
    } else {
      resp.setContentLength(0);
    }
  }
  
  private String getStackTrace(Exception e) 
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    pw.close();
    return sw.toString();
  }
  
  public void service(HttpServletRequest req,
                      HttpServletResponse resp)
    throws IOException, ServletException
  {
    String method = req.getMethod();
    String path = req.getPathInfo();
    String query = req.getQueryString();
    
    ClassificationResult classification =
      processor.classifyRequest(method, path, query);
    
    if (supportCors != Cors.NONE) {
      addCorsHeaders(req, resp);
    }
    switch (classification.getStatus()) {
    case INVALID_PATH:
      sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Invalid path " + path);
      break;
    case INVALID_METHOD:
      resp.setHeader("Allow", makeAllowHeader(classification));
      sendError(resp, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method not allowed: " + method);
      break;
    case REQUIRED_QPS_MISSING:
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Required query parameters missing: " + classification.getMissingQueryParams());
      break;
    case INVALID_QP_VALUE:
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Bad query parameter value");
      break;
    case EXTRA_QPS:
      processRequest(classification, req, resp);
      break;
    case SUCCESSFUL:
      if (req.getMethod().equalsIgnoreCase("OPTIONS")) {
        resp.setHeader("Allow", makeAllowHeader(classification));
        if (supportCors != Cors.NONE) {
          addOptionsCorsHeaders(req, resp, classification);
        }
      } else {
        processRequest(classification, req, resp);
      }
      break;
    default:
      throw new AssertionError();
    }
  }
  
  private void processRequest(ClassificationResult classification, HttpServletRequest req,
                              HttpServletResponse resp)
    throws IOException, ServletException
  {
    Object context = classification.getContext();
    if (context == null) {
      if (verboseErrors) {
        // For now, just return a default response
        resp.setContentType("text/plain");
        resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
        PrintWriter writer = resp.getWriter();
        writer.println("No handler set for API call:");
        writer.println("  Resource: (" + classification.getResource().getNodeId() +
            ") " + classification.getResource().getPath());
        writer.println("  Operation: (" + classification.getOperation().getNodeId() + ") " +
            classification.getOperation().getMethod());
        writer.close();
      } else {
        sendError(resp, HttpServletResponse.SC_NOT_IMPLEMENTED, "No handler implemented");
      }
    
    } else if (context instanceof HttpServlet) {
      HttpServlet servlet = (HttpServlet)context;
      servlet.service(req, resp);
    
    } else if (context instanceof MasterResourceHandler) {
      MasterResourceHandler handler = (MasterResourceHandler)context;
      handler.handle(classification, req, resp);
    
    } else {
      throw new AssertionError();
    }
  }
  
  private void buildAPIProcessor(String model, String mapping, ServletConfig cfg)
    throws IOException, ServletException, APIModelException
  {
    InputStream modelIn = 
      getClass().getResourceAsStream(model);
    if (modelIn == null) {
      throw new ServletException("API model \"" + model + "\" not found");
    }
    
    List<API> apis;
    try {
      if (model.endsWith(".xml") || model.endsWith(".wadl")) {
        apis = WADLReader.get().readModel("API", modelIn);
      } else if (model.endsWith(".txt") || model.endsWith(".md")) {
        apis = APITextReader.get().readModel("API", modelIn);
      } else {
        throw new ServletException("API model extension not supported for \"" + model + '\"');
      }
    } finally {
      modelIn.close();
    }
    
    log("Successfully read the API model from " + model);
    
    InputStream mappingIn =
      getClass().getResourceAsStream(mapping);
    if (mappingIn == null) {
      throw new ServletException("API mapping file \"" + mapping + "\" not found");
    }
    Properties mappingProps = new Properties();
    try {
      mappingProps.load(mappingIn);
    } finally {
      mappingIn.close();
    }
    
    log("Successfully loaded the object mapping from " + mapping);
    
    processor = APIProcessorFactory.get().createProcessor(apis);
    for (String resName : mappingProps.stringPropertyNames()) {
      String className = mappingProps.getProperty(resName);
      Class<?> contextClass;
      try {
        contextClass = getClass().getClassLoader().loadClass(className);
      } catch (ClassNotFoundException cnfe) {
        throw new APIModelException("Cannot load class \"" + className + '\"', cnfe);
      }
      
      Object newHandler;
      try {
        newHandler = contextClass.newInstance();
      } catch (InstantiationException ie) {
        throw new APIModelException("Can't create instance of \"" + className + '\"', ie);
      } catch (IllegalAccessException iae) {
        throw new APIModelException("Can't create instance of \"" + className + '\"', iae);
      }
      
      Object realHandler;
      if (newHandler instanceof HttpServlet) {
        HttpServlet handlerServlet = (HttpServlet)newHandler;
        handlerServlet.init(cfg);
        realHandler = handlerServlet;
      } else if (newHandler instanceof APIResourceHandler) {
        APIResourceHandler resHandler = (APIResourceHandler)newHandler;
        realHandler = new MasterResourceHandler(resHandler);
      } else {
        throw new ServletException("Content class \"" + className + "\" is not of the right type. " +
                                   "It must extend HttpServlet or APIResourceHandler");
      }
      
      processor.attachContext(resName, realHandler);
    }
    
    log("API processor initialized and ready to go");
  }
  
  private String makeAllowHeader(ClassificationResult r)
  {
    StringBuilder b = new StringBuilder();
    boolean once = false;
    for (String method : r.getSupportedMethods()) {
      if (once) {
        b.append(',');
      } else {
        once = true;
      }
      b.append(method);
    }
    return b.toString();
  }
  
  private void addCorsHeaders(HttpServletRequest req, HttpServletResponse resp)
  {
    String origin = req.getHeader("Origin");
    if (origin == null) {
      return;
    }
    
    if (supportCors == Cors.AUTHENTICATED) {
      // According to the spec, this is what we do because we support credentials
      resp.setHeader("Access-Control-Allow-Origin", origin);
      resp.setHeader("Access-Control-Allow-Credentials", "true");
    } else if (supportCors == Cors.ON) {
      resp.setHeader("Access-Control-Allow-Origin", "*");
    }
  }
  
  private void addOptionsCorsHeaders(HttpServletRequest req, HttpServletResponse resp,
                                     ClassificationResult classification)
  {
    if (req.getHeader("Origin") == null) {
      return;
    }
    
    String method = req.getHeader("Access-Control-Request-Method");
    if (classification.getSupportedMethods().contains(method)) {
      resp.setHeader("Access-Control-Allow-Methods", makeAllowHeader(classification));
    }
  }
}
