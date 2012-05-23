package com.apigee.apimodel.api;

import java.io.IOException;

import javax.persistence.EntityManager;

import com.apigee.apimodel.impl.OrganizationImpl;
import com.apigee.apimodel.servlet.APIRequest;
import com.apigee.apimodel.servlet.APIResourceException;
import com.apigee.apimodel.servlet.APIResponse;

public class OrganizationResource
  extends GenericResource
{
  public Class<?> getRequestClass() {
    return OrganizationImpl.class;
  }
  
  public Object get(APIRequest req, APIResponse resp, EntityManager em)
    throws IOException, APIResourceException
  {
    String orgName = req.getTemplateParameter("org");
    assert orgName != null;
    
    OrganizationImpl org = em.find(OrganizationImpl.class, orgName);
    if (org == null) {
      throw new APIResourceException(404);
    }
    return org;
  }
  
  public Object delete(APIRequest req, APIResponse resp, EntityManager em)
    throws APIResourceException
  {
    String orgName = req.getTemplateParameter("org");
    assert orgName != null;
    
    OrganizationImpl org = em.find(OrganizationImpl.class, orgName);
    if (org == null) {
      throw new APIResourceException(404);
    }
    
    em.remove(org);
    return null;
  }
}
