package com.apigee.apimodel.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIWriter;
import com.apigee.apimodel.apigeeconsole.ApigeeJsonWriter;
import com.apigee.apimodel.apitext.APITextWriter;
import com.apigee.apimodel.console.ConsoleWriter;
import com.apigee.apimodel.impl.APIImpl;
import com.apigee.apimodel.impl.OrganizationImpl;
import com.apigee.apimodel.iodocs.IODocsWriter;
import com.apigee.apimodel.servlet.APIRequest;
import com.apigee.apimodel.servlet.APIResourceException;
import com.apigee.apimodel.servlet.APIResponse;
import com.apigee.apimodel.swagger.SwaggerJSONWriter;
import com.apigee.apimodel.wadl.WADLWriter;

public class APIResource 
  extends AbstractAPIResource
{
  public Class<?> getRequestClass() {
    return InputStream.class;
  }
  
  private APIImpl findApi(String orgName, String apiName, EntityManager em)
    throws APIResourceException
  {
    Query q = em.createQuery("select a from APIImpl a, OrganizationImpl o " +
                             "where o.name = :org and a.name = :api and a.parentOrganization = o",
                             APIImpl.class);
    q.setParameter("org", orgName);
    q.setParameter("api", apiName);
    List<APIImpl> apis = q.getResultList();
    if (apis.isEmpty()) {
      throw new APIResourceException("API " + apiName + " not found", 404);
    }
    return apis.get(0);
  }
  
  public void getApisize(String orgName,String apiName,EntityManager em)
  {
	  Query q = em.createQuery("select a from APIImpl a, OrganizationImpl o " +
			  "where o.name = :org and a.name = :api and a.parentOrganization = o",
			  APIImpl.class);
	  q.setParameter("org", orgName);
	  q.setParameter("api", apiName);
	  List<APIImpl> apis = q.getResultList();
	  if(apis != null)
	  {
		  for(APIImpl api:apis)
		  {			  
		  em.remove(api);
		  }
	  }
	  
  }
  
  public Object get(APIRequest apiReq, APIResponse apiResp, EntityManager em)
    throws IOException, APIResourceException
  {
    String orgName = apiReq.getTemplateParameter("org");
    assert(orgName != null);
    String apiName = apiReq.getTemplateParameter("api");
    assert (apiName != null);
    
    String format = apiReq.getQueryParameter("format");
    
    API api = findApi(orgName, apiName, em);
    APIWriter wr;
    
    if (format == null) {
      return api;
    
    } else if (format.equals("wadl")) {
      apiResp.setContentType("application/xml");
      wr = WADLWriter.get();
        
    } else if (format.equals("apitext")) {
      apiResp.setContentType("text/plain");
      wr = APITextWriter.get();
      
    } else if (format.equals("swagger")) {
      apiResp.setContentType("application/json");
      wr = SwaggerJSONWriter.get();
    
    } else if (format.equals("iodocs")) {
      apiResp.setContentType("application/json");
      wr = IODocsWriter.get();
      
    } else if (format.equals("apigeeconsole")) {
      apiResp.setContentType("application/json");
      wr = ConsoleWriter.get();
        
    } else if (format.equals("apigeejson")) {
        apiResp.setContentType("application/json");
        wr = ApigeeJsonWriter.get();
          
      }
    else {
      throw new APIResourceException("Invalid format " + format, 400);
    }
    
    wr.writeModel(Collections.singletonList(api), apiResp.getOutputStream());
    return null;
  }
  
  public Object put(Object body, APIRequest req, APIResponse resp, EntityManager em)
      throws IOException, APIResourceException
    {
      String orgName = req.getTemplateParameter("org");
      assert(orgName != null);
      String name = req.getTemplateParameter("api");
      assert(name != null);
      
      String format = req.getQueryParameter("format");
      
      API api = findApi(orgName, name, em);
      
      OrganizationImpl org = em.find(OrganizationImpl.class, orgName);
      if (org == null) {
        throw new APIResourceException("Organization not found: " + orgName, 404);
      }
      
      List<API> newApis = importApi(name, format, body, req);
      if (newApis.size() > 1) {
        throw new APIResourceException(
          "This operation is not supported when the input file contains multiple APIs");
      }
      
      API newApi = newApis.get(0);
      
      org.deleteApi(name);
      em.remove(api);
      org.addApi(newApi);
      em.persist(org);
      em.persist(newApi);

      return newApi;
    }
  
  public Object delete(APIRequest apiReq, APIResponse apiResp, EntityManager em)
    throws IOException, APIResourceException
  {
    String orgName = apiReq.getTemplateParameter("org");
    assert(orgName != null);
    String apiName = apiReq.getTemplateParameter("api");
    assert (apiName != null);
    
    getApisize(orgName,apiName,em);
  /*  API api = findApi(orgName, apiName, em);
    em.remove(api);*/
    return null;
  }
}
