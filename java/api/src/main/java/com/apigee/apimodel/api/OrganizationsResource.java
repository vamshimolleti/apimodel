package com.apigee.apimodel.api;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.apigee.apimodel.impl.OrganizationImpl;
import com.apigee.apimodel.servlet.APIRequest;
import com.apigee.apimodel.servlet.APIResourceException;
import com.apigee.apimodel.servlet.APIResponse;

public class OrganizationsResource
  extends GenericResource
{
  public Class<?> getRequestClass() {
    return Map.class;
  }
  
  public Object get(APIRequest req, APIResponse resp, EntityManager em)
    throws IOException
  {
    ArrayList<HashMap<String, Object>> json = new ArrayList<HashMap<String, Object>>();
    
    Query q = em.createQuery("select x from OrganizationImpl x order by x.name",
                             OrganizationImpl.class);
      
    for (OrganizationImpl o : (List<OrganizationImpl>)q.getResultList()) {
      HashMap<String, Object> obj = new HashMap<String, Object>();
      obj.put("name", o.getName());
      obj.put("link", "/o/" + o.getName());
      json.add(obj);
    }
    return json;
  }
  
  public Object post(Object body, APIRequest req, APIResponse resp, EntityManager em)
    throws IOException, APIResourceException
  {
    OrganizationImpl org = new OrganizationImpl();
    Map bodyMap = (Map)body;
    if (!bodyMap.containsKey("name")) {
      throw new APIResourceException("Missing parameter \"name\"", 400);
    }
    String[] name = (String[])bodyMap.get("name");
    org.setName(name[0]);
    
    if (em.find(OrganizationImpl.class, org.getName()) != null) {
      throw new APIResourceException("Organization \"" + name + "\" already exists", 409);
    }
    em.persist(org);
    
    resp.setResponseCode(201);
    resp.setHeader("Location", "/" + URLEncoder.encode(org.getName(), "UTF-8"));
    return org;
  }
}
