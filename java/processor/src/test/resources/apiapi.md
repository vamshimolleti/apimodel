# The API Model API

This is an API that implements an API model. It allows you to upload
and download API model descriptions. More interestingly, it also
lets you browse the model using a REST API.

This document also serves as the documentation for the API. It is written in Markdown format,
and on top of that, one of the API model processors in this application is able to
parse the "code" portions of the document as an API model description. The next few lines
set this up.

    Name = apihub
    BasePath = http://apihub.apigeng.com
    
The top-level resource in the API is a list of organizations. Each organization owns a set of
API descriptions. The organization named "apigee" is special, and will be used by us
to maintain official API descriptions.

    resource organizations

Return a list of organizations.

      operation GET
      
Create a new organization.

      operation POST

The top-level resource named "o" is a synonym for the "organizations" collection and is used elsewhere
in the API.
      
    resource o
    
Return a list of organizations.

      operation GET
      
Create a new organization.

      operation POST
      
    resource o/{org}
      request
        templateParam org (string)
        
Get a list of all the APIs defined for a single organization.        
        
      operation GET
      
Delete this organization and all its APIs.

      operation DELETE

    resource o/{org}/apis
      request
        templateParam org (string)
        
Return a list of names and links to all the APIs in the organization.

      operation GET
      
Create a new API by importing an API description. The "name" parameter specifies the name of the
newly-imported API in the model, and the "format" parameter must match one of supported
API model formats.

      operation POST
        request
          queryParam format (string, required)
            valid apitext
            valid iodocs
            valid swagger
            valid wadl
          queryParam name (string, required)

    resource o/{org}/apis/{api}
      request
        templateParam org (string)
        templateParam api (string)
        
Retrieve information about an API in the organization. By default it returns a summary of the
API and its top-level resources in the default format (either XML or JSON depending on the
value of the Accept header). If the "format" parameter is specified, however, it returns
a description of the API in the specified format.

      operation GET
        request
          queryParam format (string, optional)
            valid apitext
            valid apigeeconsole
            valid iodocs
            valid swagger
            valid wadl
         
Delete the API and all its sub-resources and operations.

       operation DELETE

A collection of high-level descriptions of the resources in the API. This URL exists
for Swagger compatibility and will return a document in the JSON format expected by 
Swagger.
       
    resource o/{org}/apis/{api}/resources.json
      request
        templateParam org (string)
        templateParam api (string)
      operation GET
      
A collection of high-level descriptions of the resources in the API.

    resource o/{org}/apis/{api}/resources
      request
        templateParam org (string)
        templateParam api (string)
      operation GET
        queryParam path (string, optional)
        
Return details of a particular resource in the API. Resources are
identified by unique numeric Ids.

    resource o/{org}/apis/{api}/resources/{resource}
      request
        templateParam org (string)
        templateParam api (string)
        templateParam resource (int)
      operation GET

Return a collection of the operations supported by a particular resource.

    resource o/{org}/apis/{api}/resources/{resource}/operations
      request
        templateParam org (string)
        templateParam api (string)
        templateParam resource (int)
      operation GET
        queryParam method (string, optional)
        
Return information on a particular operation. Operations are identified by 
a unique numeric id.

    resource o/{org}/apis/{api}/resources/{resource}/operations/{operation}
      request
        templateParam org (string)
        templateParam api (string)
        templateParam resource (int)
        templateParam operation (int)
      operation GET

