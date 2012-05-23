package com.apigee.apimodel.servlet;

import java.io.IOException;

import javax.persistence.EntityManager;

public abstract class APIResourceHandler
{
  public abstract Class<?> getRequestClass();
  
  public String getPersistenceUnit() {
    return null;
  }
  
  public Object get(APIRequest req, APIResponse resp, EntityManager em)
    throws APIResourceException, IOException
  {
    return get(req, resp);
  }
  
  public Object get(APIRequest req, APIResponse resp)
    throws APIResourceException, IOException
  {
    return get();
  }
  
  public Object get()
    throws APIResourceException, IOException
  {
    throw new APIResourceException(501);
  }
  
  public Object delete(APIRequest req, APIResponse resp, EntityManager em)
    throws APIResourceException, IOException
  {
    return delete(req, resp);
  }
  
  public Object delete(APIRequest req, APIResponse resp)
    throws APIResourceException, IOException
  {
    return delete();
  }
  
  public Object delete()
    throws APIResourceException, IOException
  {
    throw new APIResourceException(501);
  }
  
  public Object put(Object body, APIRequest req, APIResponse resp, EntityManager em)
    throws APIResourceException, IOException
  {
    return put(body, req, resp);
  }
  
  public Object put(Object body, APIRequest req, APIResponse resp)
    throws APIResourceException, IOException
  {
    return put(body);
  }
  
  public Object put(Object body)
    throws APIResourceException, IOException
  {
    throw new APIResourceException(501);
  }
  
  public Object post(Object body, APIRequest req, APIResponse resp, EntityManager em)
    throws APIResourceException, IOException
  {
    return post(body, req, resp);
  }
  
  public Object post(Object body, APIRequest req, APIResponse resp)
    throws APIResourceException, IOException
  {
    return post(body);
  }
  
  public Object post(Object body)
    throws APIResourceException, IOException
  {
    throw new APIResourceException(501);
  }
  
  public Object patch(Object body, APIRequest req, APIResponse resp, EntityManager em)
    throws APIResourceException, IOException
  {
    return patch(body, req, resp);
  }
  
  public Object patch(Object body, APIRequest req, APIResponse resp)
    throws APIResourceException, IOException
  {
    return patch(body);
  }
  
  public Object patch(Object body)
    throws APIResourceException, IOException
  {
    throw new APIResourceException(501);
  }
}
