package com.apigee.apimodel.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.apigee.apimodel.impl.ResourceImpl;
import com.apigee.apimodel.servlet.APIRequest;
import com.apigee.apimodel.servlet.APIResourceException;
import com.apigee.apimodel.servlet.APIResponse;

public class ResourcesResource 
  extends GenericResource
{ 
  public Object get(APIRequest apiReq, APIResponse apiResp, EntityManager em)
      throws IOException, APIResourceException
  {
    String orgName = apiReq.getTemplateParameter("org");
    assert(orgName != null);
    String apiName = apiReq.getTemplateParameter("api");
    assert (apiName != null);
    
    String path = apiReq.getQueryParameter("path");
    
    Query q;
    if (path == null) {
      q = em.createQuery("select r from APIImpl a, OrganizationImpl o, ResourceImpl r " +
                         "where a.name = :api and o.name = :org " +
                         "and a.parentOrganization = o and r.parentApi = a",
                         ResourceImpl.class);
    } else {
      q = em.createQuery("select r from APIImpl a, OrganizationImpl o, ResourceImpl r " +
                         "where a.name = :api and o.name = :org " +
                         "and a.parentOrganization = o and r.parentApi = a " +
                         "and r.path = :path",
                         ResourceImpl.class);
      q.setParameter("path", path);
    } 
    q.setParameter("org", orgName);
    q.setParameter("api", apiName);
      
    List<ResourceImpl> results = q.getResultList();
    ArrayList<Object> resources = new ArrayList<Object>();
    for (ResourceImpl ri : results) {
      HashMap<String, Object> res = new HashMap<String, Object>();
      res.put("path", ri.getPath());
      res.put("id", ri.getId());
      res.put("link", "/apis/" + apiName + "/resources/" + ri.getId());
      res.put("nodeId", ri.getNodeId());
      resources.add(res);
    }

    return results;
  }
}
