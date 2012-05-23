package com.apigee.apimodel.swagger;

import com.apigee.apimodel.*;
import com.wordnik.swagger.core.*;
import com.wordnik.swagger.core.util.JsonUtil;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SwaggerJSONWriter
  implements APIWriter
{
  private static final SwaggerJSONWriter writer = new SwaggerJSONWriter();

  private SwaggerJSONWriter()
  {
  }
  
  public static SwaggerJSONWriter get() {
    return writer;
  }

  /**
   * Write a list of API models out in the Swagger JSON format.
   * If list has more than one API, the JSON objects are written consecutively
   * to the output stream.
   */
  public void writeModel(List<API> models, OutputStream out, Logger log)
      throws IOException
  {
    for (API model : models) {
      try {
        writeDocumentation(apiToDocumentation(model, log), out);
      } catch (APIModelException e) {
        log.log(Level.SEVERE, "couldn't serialize documentation", e);
      }
    }
  }
  
  public void writeModel(List<API> models, OutputStream out)
      throws IOException
  {
    writeModel(models, out, Logger.getLogger(DEFAULT_WRITER_LOGGER));
  }


  /**
   * Write a Swagger documentation structure as JSON
   */
  private void writeDocumentation(Documentation doc, OutputStream out)
    throws APIModelException, IOException
  {
    try {
      JsonUtil.getJsonMapper().writeValue(out, doc);
    } catch (JsonParseException e) {
      throw new APIModelException(e);
    } catch (JsonMappingException e) {
      throw new APIModelException(e);
    }
  }

  /**
   * Convert an API to a Swagger Documentation object
   */
  private Documentation apiToDocumentation(API api, Logger log) {
    Documentation doc = new Documentation();

    doc.setSwaggerVersion(SwaggerSpec.version());
    doc.setBasePath(api.getBasePath());

    List<DocumentationEndPoint> docEndPoints = new ArrayList<DocumentationEndPoint>();

    for (Resource resource : api.getResources()) {
      DocumentationEndPoint docEndPoint = new DocumentationEndPoint();

      docEndPoint.setDescription(resource.getDocumentation());
      docEndPoint.setPath(resource.getPath());

      List<Parameter> templateParams = resource.getTemplateParameters(); // cache to add to operations later

      for (Operation operation : resource.getOperations()) {
        DocumentationOperation docOperation = new DocumentationOperation();

        docOperation.setNickname(operation.getDisplayName());
        docOperation.setSummary(operation.getDocumentation()); // TODO: break apart summary/notes?
        docOperation.setHttpMethod(operation.getMethod());

        /* errorResponses */
        for (ResponseCode responseCode : operation.getResponseCodes().values()) {
          // swagger only cares about error responses
          if (responseCode.isSuccess()) {
            continue;
          }

          DocumentationError docError = new DocumentationError();

          docError.setCode(responseCode.getCode());
          docError.setReason(responseCode.getMessage());

          docOperation.addErrorResponse(docError);
        }

        /* parameters */
        // TODO: matrix parameters? these aren't supported by swagger
        // combine all the parameters we want in the operation
        Set<Parameter> parameterSet = new HashSet<Parameter>();
        // query parameters
        parameterSet.addAll(operation.getQueryParameters().values());
        // operations inherit template parameters from resource
        parameterSet.addAll(templateParams);

        for (Parameter parameter : parameterSet) {
          DocumentationParameter docParameter = new DocumentationParameter();

          docParameter.setName(parameter.getName());
          docParameter.setDescription(parameter.getDocumentation()); // TODO: use notes as well?
          docParameter.setDefaultValue(parameter.getDefault());
          docParameter.setRequired(parameter.isRequired());
          docParameter.setAllowMultiple(parameter.isRepeating());
          docParameter.setDataType(parameter.getDataType());

          List<String> allowableValues = new ArrayList<String>();
          for (ValidValue validValue : parameter.getValidValues()) {
            allowableValues.add(validValue.getValue());
          }

          docParameter.setAllowableValues(new DocumentationAllowableListValues(allowableValues));

          switch (parameter.getType()) {
            case TEMPLATE:
              docParameter.setParamType("path");
              break;
            case QUERY:
              docParameter.setParamType("query");
              break;
            case MATRIX:
            case REQUEST_HEADER:
            case RESPONSE_HEADER:
              log.warning("unsupported parameter type: " + parameter.getType());
              continue; // don't add this parameter
          }

          // TODO: error response stuff?
          // TODO: parameter access property

          docOperation.addParameter(docParameter);
        }

        docEndPoint.addOperation(docOperation);
      }

      docEndPoints.add(docEndPoint);
    }

    doc.setApis(docEndPoints);

    return doc;
  }

}
