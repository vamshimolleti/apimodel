package com.apigee.apimodel.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIModelException;
import com.apigee.apimodel.APIReader;
import com.apigee.apimodel.apitext.APITextReader;
import com.apigee.apimodel.iodocs.IODocsReader;
import com.apigee.apimodel.processor.APIProcessor.ContentType;
import com.apigee.apimodel.servlet.APIRequest;
import com.apigee.apimodel.servlet.APIResourceException;
import com.apigee.apimodel.swagger.SwaggerJSONReader;
import com.apigee.apimodel.wadl.WADLReader;

public class AbstractAPIResource
  extends GenericResource
{
  protected List<API> importApi(String name, String format, Object body, APIRequest req)
    throws APIResourceException, IOException
  {
    assert(name != null);
    assert(format != null);
    
    try {
      APIReader rdr;
      if (format.equals("wadl") && 
          (req.getParsedContentType() == ContentType.XML)) {
        rdr = WADLReader.get();
        
      } else if (format.equals("apitext") && 
                 (req.getParsedContentType() == ContentType.TEXT)) {
        rdr = APITextReader.get();
        
      } else if (format.equals("swagger") &&
                 (req.getParsedContentType() == ContentType.JSON)) {
        rdr = SwaggerJSONReader.get();
        
      } else if (format.equals("iodocs") &&
                 (req.getParsedContentType() == ContentType.JSON)) {
        rdr = IODocsReader.get();
        
      } else {
        throw new APIResourceException("Invalid input format or content type", 400);
      }
      
      return rdr.readModel(name, (InputStream)body);
      
    } catch (APIModelException apie) {
      throw new APIResourceException("Error importing API model", 400);
    }
  }
}
