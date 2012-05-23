package com.apigee.apimodel.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.apigee.apimodel.impl.OperationImpl;
import com.apigee.apimodel.servlet.APIRequest;
import com.apigee.apimodel.servlet.APIResourceException;
import com.apigee.apimodel.servlet.APIResponse;

public class OperationsResource 
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
      em.createQuery("select op from APIImpl a, OrganizationImpl o, ResourceImpl r, OperationImpl op " +
                     "where a.name = :api and o.name = :org and r.id = :resource " +
                     "and a.parentOrganization = o and r.parentApi = a and op.resource = r",
                     OperationImpl.class);
    q.setParameter("api", apiName);
    q.setParameter("org", orgName);
    q.setParameter("resource", Long.valueOf(resId));
      
    List<OperationImpl> results = q.getResultList();
    ArrayList<Object> ops = new ArrayList<Object>();
    for (OperationImpl op : results) {
      HashMap<String, Object> res = new HashMap<String, Object>();
      res.put("method", op.getMethod());
      res.put("link", "/apis/" + apiName + "/resources/" + resId + "/operations/" + op.getId());
      res.put("nodeId", op.getNodeId());
      ops.add(res);
    }

    return ops;
  }
}
