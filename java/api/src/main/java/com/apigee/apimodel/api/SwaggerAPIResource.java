package com.apigee.apimodel.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.apigee.apimodel.impl.APIImpl;
import com.apigee.apimodel.servlet.APIRequest;
import com.apigee.apimodel.servlet.APIResourceException;
import com.apigee.apimodel.servlet.APIResponse;

public class SwaggerAPIResource extends GenericResource
{
  public Object get(APIRequest req, APIResponse resp, EntityManager em)
    throws IOException, APIResourceException
  {
    String orgName = req.getTemplateParameter("org");
    assert(orgName != null);
    String apiName = req.getTemplateParameter("api");
    assert (apiName != null);
    
    Query q = em.createQuery("select a from APIImpl a, OrganizationImpl o " +
                             "where o.name = :org and a.name = :api and a.parentOrganization = o",
                             APIImpl.class);
    q.setParameter("org", orgName);
    q.setParameter("api", apiName);
    List<APIImpl> apis = q.getResultList();
    if (apis.isEmpty()) {
      throw new APIResourceException("API " + apiName + " not found", 404);
    }
    
    HashMap<String, Object> json = new HashMap<String, Object>();
    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    json.put("apis", list);
    for (APIImpl a : apis) {
      HashMap<String, Object> ja = new HashMap<String, Object>();
      ja.put("name", a.getName());
      ja.put("path", "?format=swagger");
      ja.put("description", a.getDocumentation());
      ja.put("basePath", a.getBasePath());
      ja.put("swaggerVersion", "1.1");
      list.add(ja);
    }
    
    return json;
  }
}
