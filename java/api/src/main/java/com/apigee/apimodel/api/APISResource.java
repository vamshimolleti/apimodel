package com.apigee.apimodel.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.apigee.apimodel.API;
import com.apigee.apimodel.impl.APIImpl;
import com.apigee.apimodel.impl.OrganizationImpl;
import com.apigee.apimodel.servlet.APIRequest;
import com.apigee.apimodel.servlet.APIResourceException;
import com.apigee.apimodel.servlet.APIResponse;

public class APISResource
  extends AbstractAPIResource
{  
  public Class<?> getRequestClass() {
    return InputStream.class;
  }
  
  public Object get(APIRequest req, APIResponse resp, EntityManager em)
    throws IOException, APIResourceException
  {
    String orgName = req.getTemplateParameter("org");
    assert(orgName != null);
    
    
    Query q = em.createQuery("select a from APIImpl a, OrganizationImpl o " +
                             "where o.name = :org and a.parentOrganization = o " +
                             "order by a.name",
                             APIImpl.class);
    q.setParameter("org", orgName);
    List<APIImpl> apis = q.getResultList();
    
    ArrayList<HashMap<String, Object>> json = new ArrayList<HashMap<String, Object>>();
    for (APIImpl a : apis) {
      HashMap<String, Object> ja = new HashMap<String, Object>();
      ja.put("name", a.getName());
      ja.put("link", "/o/" + orgName + "/apis/" + a.getName());
      json.add(ja);
    }
    return json;
  }
  
  public Object post(Object body, APIRequest req, APIResponse resp, EntityManager em)
    throws IOException, APIResourceException
  {
    String orgName = req.getTemplateParameter("org");
    assert(orgName != null);
    
    String name = req.getQueryParameter("name");
    String format = req.getQueryParameter("format");
    
    OrganizationImpl org = em.find(OrganizationImpl.class, orgName);
    if (org == null) {
      throw new APIResourceException("Organization not found: " + orgName, 404);
    }
    
    List<API> apis = importApi(name, format, body, req);
    
    findApi(orgName,name,em);
    int i = 0;
    for (API a : apis) {
      if (i > 0) {
        a.setName(a.getName() + i);
      }
      org.addApi(a);
      em.persist(org);
      em.persist(a);
    }
    
    resp.setResponseCode(201);
    resp.setHeader("Location", "/o/" + orgName + "/apis/" + name);
    return apis;
  }
  
  private void findApi(String orgName, String apiName, EntityManager em)
		    throws APIResourceException
		  {
		    Query q = em.createQuery("select a from APIImpl a, OrganizationImpl o " +
		                             "where o.name = :org and a.name = :api and a.parentOrganization = o",
		                             APIImpl.class);
		    q.setParameter("org", orgName);
		    q.setParameter("api", apiName);
		    List<APIImpl> apis = q.getResultList();
		    if (!apis.isEmpty()) {
		      for(APIImpl api : apis)
		      {
		    	  em.remove(api);
		      }
		    }
		  }
}
