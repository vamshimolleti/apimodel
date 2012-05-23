package com.apigee.apimodel.api;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.apigee.apimodel.impl.OperationImpl;
import com.apigee.apimodel.servlet.APIRequest;
import com.apigee.apimodel.servlet.APIResourceException;
import com.apigee.apimodel.servlet.APIResponse;

public class OperationResource
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
    String opId = apiReq.getTemplateParameter("operation");
    assert (opId != null);
    
    Query q =
      em.createQuery("select op from APIImpl a, OrganizationImpl o, ResourceImpl r, OperationImpl op " +
                     "where a.name = :api and o.name = :org and r.id = :resource " +
                     "and a.parentOrganization = o and r.parentApi = a and op.resource = r " +
                     "and op.id = :operation",
                     OperationImpl.class);
    q.setParameter("api", apiName);
    q.setParameter("org", orgName);
    q.setParameter("resource", Long.valueOf(resId));
    q.setParameter("operation", Long.valueOf(opId));
      
    List<OperationImpl> results = q.getResultList();
    if (results.isEmpty()) {
      throw new APIResourceException("Operation " + opId + " not found", 404);
    }
    return results.get(0);
  }
}
