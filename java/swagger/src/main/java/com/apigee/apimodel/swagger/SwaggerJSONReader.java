package com.apigee.apimodel.swagger;

import com.apigee.apimodel.*;
import com.apigee.apimodel.impl.*;
import com.wordnik.swagger.core.*;
import com.wordnik.swagger.core.util.JsonUtil;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

public class SwaggerJSONReader
  implements APIReader
{
  private static final SwaggerJSONReader reader = new SwaggerJSONReader();

  private SwaggerJSONReader()
  {
  }

  public static SwaggerJSONReader get() {
    return reader;
  }

  public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
    return iterable == null ? Collections.<T>emptyList() : iterable;
  }

  /**
   * Read in a Swagger JSON model. Most useful for operations listings.
   * @return a List with a single API object
   */
  public List<API> readModel(String name, InputStream in, Logger log)
    throws APIModelException, IOException
  {
    Documentation doc = readDocumentation(in);
    List<API> apis = new ArrayList<API>();

    apis.add(documentationToApi(name, doc, log));

    return apis;
  }

  public List<API> readModel(String name, InputStream in)
    throws APIModelException, IOException
  {
    return readModel(name, in, Logger.getLogger(DEFAULT_READER_LOGGER));
  }

  /**
   * Read in the JSON stream and map it to the base Swagger documentation structure
   */
  private Documentation readDocumentation(InputStream in)
    throws APIModelException, IOException
  {
    Documentation doc;

    try {
      doc = JsonUtil.getJsonMapper().readValue(in, Documentation.class);
    } catch (JsonParseException e) {
      throw new APIModelException(e);
    } catch (JsonMappingException e) {
      throw new APIModelException(e);
    }

    if (doc == null) {
      throw new APIModelException("documentation is null");
    }

    return doc;
  }

  /**
   * Convert a Swagger Documentation object into an API
   */
  private API documentationToApi(String name, Documentation doc, Logger log)
    throws APIModelException
  {
    API api = new APIImpl();

    api.setName(name);
    api.setBasePath(doc.getBasePath());

    for (DocumentationEndPoint docEndpoint : emptyIfNull(doc.getApis())) {
      Resource resource = new ResourceImpl();

      resource.setDocumentation(docEndpoint.getDescription());
      resource.setPath(docEndpoint.getPath());

      // cache of resource template parameter names to avoid adding duplicates from operations
      Set<String> resourceTemplateParameters = new HashSet<String>();

      for (DocumentationOperation docOperation : emptyIfNull(docEndpoint.getOperations())) {
        Operation operation = new OperationImpl();

        operation.setResource(resource);

        // operation.setNodeId(docOperation.getNickname());
        operation.setDisplayName(docOperation.getNickname());
        operation.setDocumentation(docOperation.getSummary()); // TODO: use notes as well?
        operation.setMethod(docOperation.getHttpMethod());

        /* errorResponses */
        for (DocumentationError docError : emptyIfNull(docOperation.getErrorResponses())) {
          ResponseCode responseCode = new ResponseCodeImpl();

          responseCode.setCode(docError.getCode());
          responseCode.setMessage(docError.getReason());
          responseCode.setSuccess(false);

          operation.addResponseCode(responseCode);
        }

        /* parameters */
        for (DocumentationParameter docParameter : emptyIfNull(docOperation.getParameters())) {
          Parameter parameter = new ParameterImpl();

          parameter.setName(docParameter.getName());
          parameter.setDocumentation(docParameter.getDescription()); // TODO: use notes as well?
          parameter.setDefault(docParameter.getDefaultValue());
          parameter.setRequired(docParameter.getRequired());
          parameter.setRepeating(docParameter.allowMultiple());
          parameter.setDataType(docParameter.getDataType());

          // FIXME!!! jackson doesn't object map into Allowable(List|Range)Values -- just the interface class
          /*
          DocumentationAllowableValues docAllowableValues = docParameter.getAllowableValues();
          if (docAllowableValues instanceof DocumentationAllowableListValues) {
            for (String allowableValue :
                ((DocumentationAllowableListValues) docAllowableValues).getValues()) {
              ValidValue validValue = new ValidValueImpl();
              validValue.setValue(allowableValue);
              parameter.addValidValue(validValue);
            }
          }
          */

          // TODO: "post" type
          String paramType = docParameter.getParamType();
          if (paramType.equals("path")) {
            parameter.setType(Parameter.Type.TEMPLATE);
            // add the template parameter to the resource if it's not there already
            if (!resourceTemplateParameters.contains(parameter.getName())) {
              resourceTemplateParameters.add(parameter.getName());

              resource.addTemplateParameter(parameter);
            }
          } else if (paramType.equals("query")) {
            parameter.setType(Parameter.Type.QUERY);
            operation.addQueryParameter(parameter);
          } else {
            log.warning("unsupported parameter type: " + paramType);
          }
        }

        resource.addOperation(operation);
      }

      api.addResource(resource);
    }

    return api;
  }
}
