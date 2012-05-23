package com.apigee.apimodel.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import com.apigee.apimodel.processor.ClassificationResult;

public class MasterResourceHandler 
{
  private final APIResourceHandler handler;
  private final ObjectMapper mapper = new ObjectMapper();
  
  public MasterResourceHandler(APIResourceHandler handler)
  {
    this.handler = handler;
    mapper.configure(Feature.INDENT_OUTPUT, true);
  }
  
  public void handle(ClassificationResult classification,
                     HttpServletRequest req, HttpServletResponse resp)
    throws IOException
  {
    Object requestBody = null;
    Object responseBody;
    
    APIRequest apiReq = new APIRequest(req, classification);
    APIResponse apiResp = new APIResponse(req, resp, classification);
    EntityManager em = null;
    
    try {  
      if ((req.getMethod().equals("PUT") || req.getMethod().equals("POST") || 
          req.getMethod().equals("PATCH")) && (handler.getRequestClass() != null)) {
        if (InputStream.class.isAssignableFrom(handler.getRequestClass())) {
          requestBody = req.getInputStream();
        } else if (Reader.class.isAssignableFrom(handler.getRequestClass())) {
          requestBody = req.getReader();
        } else {
          requestBody = readBody(req, apiReq, handler.getRequestClass());
        }
      }

      if (handler.getPersistenceUnit() != null) {
        EntityManagerFactory factory = 
          EntityManagerManager.get().getFactory(handler.getPersistenceUnit());
        em = factory.createEntityManager();
        em.getTransaction().begin();
      }

      if (req.getMethod().equals("GET")) {
        responseBody = handler.get(apiReq, apiResp, em);
      } else if (req.getMethod().equals("PUT")) {
        responseBody = handler.put(requestBody, apiReq, apiResp, em);
      } else if (req.getMethod().equals("POST")) {
        responseBody = handler.post(requestBody, apiReq, apiResp, em);
      } else if (req.getMethod().equals("DELETE")) {
        responseBody = handler.delete(apiReq, apiResp, em);
      } else if (req.getMethod().equals("PATCH")) {
        responseBody = handler.patch(requestBody, apiReq, apiResp, em);
      } else {
        throw new APIResourceException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
      }
      
      if (em != null) {
        em.getTransaction().commit();
      }
      
      if (!apiResp.isResponseCommitted()) {
        apiResp.apply();
        
        if (responseBody != null) {
          writeBody(responseBody, apiResp, resp);
        }
      }
      
    } catch (APIResourceException ae) {
      if (em != null) {
        em.getTransaction().rollback();
      }
      HashMap<String, Object> err = new HashMap<String, Object>();
      err.put("status", ae.getResponseCode());
      if (ae.getMessage() != null) {
        err.put("message", ae.getMessage());
      }
      resp.setStatus(ae.getResponseCode());
      writeBody(err, apiResp, resp);
      
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }
  
  private Object readBody(HttpServletRequest req, APIRequest apiReq, Class<?> requestClass)
    throws IOException, APIResourceException
  {
    switch (apiReq.getParsedContentType()) {
    case JSON:
      return mapper.readValue(req.getInputStream(), requestClass);
    case FORM:
      if (Map.class.isAssignableFrom(requestClass)) {
        return req.getParameterMap();
      }
      return null;
    default:
      // TODO XML
      throw new APIResourceException("Invalid content type", HttpServletResponse.SC_BAD_REQUEST);
    }
  }
  
  private void writeBody(Object response, APIResponse apiResp,
                         HttpServletResponse resp)
    throws IOException
  {
    // TODO XML!
    resp.setContentType("application/json");
    mapper.writeValue(resp.getOutputStream(), response);
  }
}

