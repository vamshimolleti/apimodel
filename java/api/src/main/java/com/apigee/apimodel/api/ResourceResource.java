package com.apigee.apimodel.api;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.apigee.apimodel.impl.ResourceImpl;
import com.apigee.apimodel.servlet.APIRequest;
import com.apigee.apimodel.servlet.APIResourceException;
import com.apigee.apimodel.servlet.APIResponse;

public class ResourceResource
  extends GenericResource
{
  public Object get(APIRequest apiReq, APIResponse apiResp, EntityManager em)
      throws IOException, APIResourceException
  {
    String orgName = apiReq.getTemplateParameter("org");
    assert(orgName != null);
    String apiName = apiReq.getTemplateParameter("api");
    assert (apiName != null);
    String resId = apiReq.getTemplateParameter("resource");
    assert (resId != null);
    
    Query q =
      em.createQuery("select r from APIImpl a, OrganizationImpl o, ResourceImpl r " +
                     "where a.name = :api and o.name = :org " +
                     "and a.parentOrganization = o and r.parentApi = a " +
                     "and r.id = :id",
                     ResourceImpl.class);
    q.setParameter("org", orgName);
    q.setParameter("api", apiName);
    q.setParameter("id", Long.valueOf(resId));
    
    List<ResourceImpl> results = q.getResultList();
    if (results.isEmpty()) {
      throw new APIResourceException("Resource " + resId + " not found", 404);
    }
    return results.get(0);
  }
}
