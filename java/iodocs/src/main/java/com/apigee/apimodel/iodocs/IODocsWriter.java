package com.apigee.apimodel.iodocs;

import com.apigee.apimodel.*;
import com.apigee.apimodel.iodocs.model.IOAPI;
import com.apigee.apimodel.iodocs.model.IOEndpoint;
import com.apigee.apimodel.iodocs.model.IOMethod;
import com.apigee.apimodel.iodocs.model.IOParameter;
import com.apigee.apimodel.iodocs.model.impl.IOAPIImpl;
import com.apigee.apimodel.iodocs.model.impl.IOEndpointImpl;
import com.apigee.apimodel.iodocs.model.impl.IOMethodImpl;
import com.apigee.apimodel.iodocs.model.impl.IOParameterImpl;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IODocsWriter
  implements APIWriter
{
  private static final IODocsWriter writer = new IODocsWriter();
  private static final ObjectMapper mapper = new ObjectMapper();

  static {
    mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
    mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
  }

  private IODocsWriter()
  {
  }
  
  public static IODocsWriter get() {
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
    try {
      writeIOApi(apisToIOApi(models, log), out);
    } catch (APIModelException e) {
      log.log(Level.SEVERE, "couldn't serialize I/O Docs API", e);
    }
  }

  public void writeModel(List<API> models, OutputStream out)
    throws IOException
  {
    writeModel(models, out, Logger.getLogger(DEFAULT_WRITER_LOGGER));
  }

  /**
   * Write an I/O Docs API object out as JSON
   */
  private void writeIOApi(IOAPI ioapi, OutputStream out)
    throws APIModelException, IOException
  {
    try {
      mapper.writeValue(out, ioapi);
    } catch (JsonParseException e) {
      throw new APIModelException(e);
    } catch (JsonMappingException e) {
      throw new APIModelException(e);
    }
  }

  /**
   * Convert a list of internal APIs to an I/O Docs API
   */
  private IOAPI apisToIOApi(List<API> apis, Logger log) {
    IOAPI ioapi = new IOAPIImpl();

    for (API api : apis) {
      IOEndpoint ioEndpoint = new IOEndpointImpl();

      ioEndpoint.setName(api.getName());
      ioEndpoint.setPath(api.getBasePath());

      for (Resource resource : api.getResources()) {
        for (Operation operation : resource.getOperations()) {
          IOMethod ioMethod = new IOMethodImpl();

          ioMethod.setURI(resource.getPath());

          ioMethod.setMethodName(operation.getDisplayName());
          ioMethod.setHTTPMethod(operation.getMethod());
          ioMethod.setSynopsis(operation.getDocumentation());

          for (Parameter parameter : operation.getQueryParameters().values()) {
            IOParameter ioParameter = new IOParameterImpl();

            ioParameter.setName(parameter.getName());
            ioParameter.setRequired(parameter.isRequired());

            // convert null to empty string
            String def = parameter.getDefault();
            ioParameter.setDefault(def == null ? "" : def);

            ioParameter.setType(parameter.getDataType());
            ioParameter.setDescription(parameter.getDocumentation());

            for (ValidValue validValue : parameter.getValidValues()) {
              ioParameter.addEnumeratedValue(validValue.getValue());
            }

            ioMethod.addParameter(ioParameter);
          }

          ioEndpoint.addMethod(ioMethod);
        }
      }

      ioapi.addEndpoint(ioEndpoint);
    }

    return ioapi;
  }

}
