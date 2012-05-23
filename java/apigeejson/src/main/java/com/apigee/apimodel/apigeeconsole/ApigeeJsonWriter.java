package com.apigee.apimodel.apigeeconsole;

import com.apigee.apimodel.*;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApigeeJsonWriter
  implements APIWriter
{
  private static final ApigeeJsonWriter writer = new ApigeeJsonWriter();
  private static final ObjectMapper mapper = new ObjectMapper();

  static {
 //   mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
    mapper.configure(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS, false);
    mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
    mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
  }

  private ApigeeJsonWriter()
  {
  }

  public static ApigeeJsonWriter get()
  {
    return writer;
  }

  /**
   * Write a list of API models out in the Apigee Console JSON format.
   */
  public void writeModel(List<API> models, OutputStream out, Logger log)
    throws IOException
  {
    try {
      writeConsoleJSON(apisToApplicationJSON(models, log), out);
    } catch (APIModelException e) {
      log.log(Level.SEVERE, "couldn't serialize Console JSON", e);
    }		
  }

  public void writeModel(List<API> models, OutputStream out)
    throws IOException
  {
    writeModel(models, out, Logger.getLogger(DEFAULT_WRITER_LOGGER));
  }

  /**
   * Write a Console JSON map (generic String->Object map) out as JSON
   */
  private void writeConsoleJSON(Map<String, Object> map, OutputStream out)
    throws APIModelException, IOException
  {
    try {
      mapper.writeValue	(out, map);
    } catch (JsonParseException e) {
      throw new APIModelException(e);
    } catch (JsonMappingException e) {
      throw new APIModelException(e);
    }
  }

  private Map<String, Object> apisToApplicationJSON(List<API> models, Logger log)
  {
    Map<String, Object> applicationJson = new HashMap<String, Object>();
    Map<String, Object> endpointsJson = new HashMap<String, Object>();
    List<Map<String, Object>> endpointList = new ArrayList<Map<String, Object>>();

    for (API model : models) {
      endpointList.add(apiToEndpointJSON(model, log));
    }
    endpointsJson.put("endpoints", endpointList);

    applicationJson.put("application", endpointsJson);

    return applicationJson;
  }

  private Map<String,Object> apiToEndpointJSON(API model, Logger log)
  {
    Map<String, Object> endpointJson = new HashMap<String, Object>();
    List<Map<String, Object>> resourcesList = new ArrayList<Map<String, Object>>();

    endpointJson.put("base", model.getBasePath());

    for (Resource resource : model.getResources()) {
      resourcesList.addAll(resourceToResourceJSONList(resource, log));
    }
    endpointJson.put("resources", resourcesList);

    return endpointJson;
  }

  private List<Map<String, Object>> resourceToResourceJSONList(Resource resource, Logger log)
  {
    List<Map<String, Object>> resourceJsonList = new ArrayList<Map<String, Object>>();

    // each operation is a separate resource
    for (Operation operation : resource.getOperations()) {
      Map<String, Object> resourceJson = new HashMap<String, Object>();
      List<Map<String, Object>> paramsList = new ArrayList<Map<String, Object>>();

      resourceJson.put("path", ensureSlashPrefix(resource.getPath()));

      /* params */
      for (Parameter templateParameter : resource.getTemplateParameters()) {
        paramsList.add(parameterToParameterJSON(templateParameter, log));
      }
      for (Parameter matrixParameter : resource.getMatrixParameters()) {
        paramsList.add(parameterToParameterJSON(matrixParameter, log));
      }
      for (Parameter queryParameter : resource.getQueryParameters().values()) {
        paramsList.add(parameterToParameterJSON(queryParameter, log));
      }
      for (Parameter requestHeader : resource.getRequestHeaders().values()) {
        paramsList.add(parameterToParameterJSON(requestHeader, log));
      }
      resourceJson.put("params", paramsList);

      resourceJson.put("method", operationToMethodJSON(operation, log));

      resourceJsonList.add(resourceJson);
    }

    return resourceJsonList;
  }


  private Map<String, Object> parameterToParameterJSON(Parameter parameter, Logger log)
  {
    Map<String, Object> parameterJson = new HashMap<String, Object>();
    List<Map<String, Object>> optionsList = new ArrayList<Map<String, Object>>();

    parameterJson.put("name", parameter.getName());
    // parameterJson.put("count", "null");
    parameterJson.put("required", parameter.isRequired());
    parameterJson.put("repeating", parameter.isRepeating());
    parameterJson.put("type", "xsd:" + parameter.getDataType()); // WADLReader gets local part for dataType

    /* style */
    String style = null;
    switch (parameter.getType()) {
      case TEMPLATE:
      case MATRIX:
      case QUERY:
        style = parameter.getType().toString().toLowerCase();
        break;
      case REQUEST_HEADER:
      case RESPONSE_HEADER:
        style = "header";
        break;
    }
    parameterJson.put("style", style);

    parameterJson.put("default", parameter.getDefault());
    parameterJson.put("doc", documentedToDocJSON(parameter, log));

    /* options */
    for (ValidValue validValue : parameter.getValidValues()) {
      optionsList.add(validValueToOptionJSON(validValue, log));
    }
    parameterJson.put("options", optionsList);

    /* ChoiceParameter stuff */
    // actual choices are already in validvalues (?)
    if (parameter instanceof ChoiceParameter) {
      ChoiceParameter choiceParameter = (ChoiceParameter) parameter;

      parameterJson.put("minCount", choiceParameter.getMinCount());
      parameterJson.put("maxCount", choiceParameter.getMaxCount());
    }

    return parameterJson;
  }

  private Map<String,Object> operationToMethodJSON(Operation operation, Logger log)
  {
    Map<String, Object> methodJson = new HashMap<String, Object>();
    Map<String, Object> tagsJson = new HashMap<String, Object>();
    Map<String, Object> authenticationJson = new HashMap<String, Object>();
    Map<String, Object> exampleJson = new HashMap<String, Object>();

    methodJson.put("id", operation.getNodeId());
    methodJson.put("name", operation.getMethod());
    methodJson.put("apigee:displayName", operation.getDisplayName());

    /* tags */
    for (Tag tag : operation.getTags()) {
      if (tag.getType().equals(Tag.PRIMARY_CATEGORY_TYPE)) {
        tagsJson.put("primary", tag.getValue());
      } else if (tag.getType().equals(Tag.CATEGORY_TYPE)) {
        // slight difference from Vamshi's converter: set value to true rather than null
        tagsJson.put(tag.getValue(), Boolean.TRUE);
      }
    }
    methodJson.put("apigee:tags", tagsJson);

    /* authentication */
    // if there are any methods specified, auth is required
    authenticationJson.put("required", !operation.getAuthenticationMethods().isEmpty());
    methodJson.put("apigee:authentication", authenticationJson);

    /* example */
    Example exampleRequest = operation.getExampleRequest();
    if (exampleRequest != null) {
      exampleJson.put("url", ensureSlashPrefix(exampleRequest.getUri()));
      exampleJson.put("value", exampleRequest.getContents());

      methodJson.put("apigee:example", exampleJson);
    }

    methodJson.put("doc", documentedToDocJSON(operation, log));

    /* request */
    Map<String, Object> requestJson = operationToRequestJSON(operation, log);
    methodJson.put("request", requestJson);
    // copy params from request
    List<Map<String, Object>> paramsJson = (List<Map<String, Object>>) requestJson.get("params");
    methodJson.put("params", paramsJson);

    /* response */
    methodJson.put("response", operationToResponseJSON(operation, log));

    return methodJson;
  }

  private Map<String,Object> operationToRequestJSON(Operation operation, Logger log)
  {
    Map<String, Object> requestJson = new HashMap<String, Object>();
    List<Map<String, Object>> paramsList = new ArrayList<Map<String, Object>>();

    // requestJson.put("doc", documentedToDocJSON(operation, log)); // FIXME? request doc same as operation doc

    /* params */
    // collect query parameters and request headers
    for (Parameter queryParameter : operation.getQueryParameters().values()) {
      paramsList.add(parameterToParameterJSON(queryParameter, log));
    }
    for (Parameter requestHeader : operation.getRequestHeaders().values()) {
      paramsList.add(parameterToParameterJSON(requestHeader, log));
    }
    requestJson.put("params", paramsList);

    Representation requestRepresentation = operation.getRequestRepresentation();
    if (requestRepresentation != null) {
      requestJson.put("representation", representationToRepresentationJSON(requestRepresentation, log));
    }

    return requestJson;
  }

  private Map<String,Object> operationToResponseJSON(Operation operation, Logger log)
  {
    Map<String, Object> responseJson = new HashMap<String, Object>();
    List<Map<String, Object>> paramsList = new ArrayList<Map<String, Object>>();
    List<Integer> statusList = new ArrayList<Integer>();

    /* status */
    for (ResponseCode responseCode : operation.getResponseCodes().values()) {
      statusList.add(responseCode.getCode());
    }
    responseJson.put("status", statusList);

    // responseJson.put("doc", documentedToDocJSON(operation, log)); // FIXME? response doc same as operation doc

    /* params */
    for (Parameter responseHeader : operation.getResponseHeaders().values()) {
      paramsList.add(parameterToParameterJSON(responseHeader, log));
    }
    responseJson.put("params", paramsList);

    Representation responseRepresentation = operation.getResponseRepresentation();
    if (responseRepresentation != null) {
      responseJson.put("representation", representationToRepresentationJSON(responseRepresentation, log));
    }

    return responseJson;
  }


  private Map<String, Object> documentedToDocJSON(Documented documented, Logger log)
  {
    Map<String, Object> docJson = new HashMap<String, Object>();

    //docJson.put("title", null);
    docJson.put("apigee:url", documented.getDocumentationURI());
    docJson.put("content", documented.getDocumentation());

    return docJson;
  }

  private Map<String, Object> validValueToOptionJSON(ValidValue validValue, Logger log)
  {
    Map<String, Object> optionJson = new HashMap<String, Object>();

    optionJson.put("mediaType", validValue.getMediaType());
    optionJson.put("value", validValue.getValue());
    optionJson.put("doc", documentedToDocJSON(validValue, log));

    return optionJson;
  }

  private Map<String, Object> representationToRepresentationJSON(Representation representation , Logger log)
  {
    Map<String, Object> representationJson = new HashMap<String, Object>();

    representationJson.put("id", representation.getNodeId());
    // representationJson.put("element", null); // FIXME?
    representationJson.put("mediaType", representation.getMediaType());
    representationJson.put("href", representation.getLink());
    // representationJson.put("profile", null); // FIXME?
    representationJson.put("apigee:payload", representation.getDefinition());

    representationJson.put("doc", documentedToDocJSON(representation, log));

    // TODO: WADL representation has params, but model one doesn't

    return representationJson;
  }

  /**
   * Prepend a forward slash ('/') to a string if it doesn't start with one already
   */
  private String ensureSlashPrefix(String str)
  {
    if (StringUtils.startsWith(str, "/")) {
      return str;
    }

    return '/' + str;
  }

}

