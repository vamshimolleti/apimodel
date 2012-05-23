package com.apigee.apimodel.iodocs;

import com.apigee.apimodel.*;
import com.apigee.apimodel.impl.*;
import com.apigee.apimodel.iodocs.model.IOAPI;
import com.apigee.apimodel.iodocs.model.IOEndpoint;
import com.apigee.apimodel.iodocs.model.IOMethod;
import com.apigee.apimodel.iodocs.model.IOParameter;
import com.apigee.apimodel.iodocs.model.impl.IOAPIImpl;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class IODocsReader
  implements APIReader
{
  private static final IODocsReader reader = new IODocsReader();
  private static final ObjectMapper mapper = new ObjectMapper();

  private IODocsReader()
  {
  }

  public static IODocsReader get() {
    return reader;
  }

  public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
    return iterable == null ? Collections.<T>emptyList() : iterable;
  }

  /**
   * Read in an I/O Docs JSON model. Each endpoint corresponds to an API.
   */
  public List<API> readModel(String name, InputStream in, Logger log)
    throws APIModelException, IOException
  {
    return ioApiToApis(name, readIOApi(in), log);
  }

  public List<API> readModel(String name, InputStream in)
    throws APIModelException, IOException
  {
    return readModel(name, in, Logger.getLogger(DEFAULT_READER_LOGGER));
  }

  /**
   * Read in the I/O Docs JSON stream and map it to an I/O Docs API object
   */
  private IOAPI readIOApi(InputStream in)
    throws APIModelException, IOException
  {
    IOAPI ioapi;

    try {
      ioapi = mapper.readValue(in, IOAPIImpl.class);
    } catch (JsonParseException e) {
      throw new APIModelException(e);
    } catch (JsonMappingException e) {
      throw new APIModelException(e);
    }

    if (ioapi == null) {
      throw new APIModelException("API is null");
    }

    return ioapi;
  }

  /**
   * Convert an I/O Docs api object into the internal API format
   */
  private List<API> ioApiToApis(String name, IOAPI ioapi, Logger log)
    throws APIModelException
  {
    List<API> apis = new ArrayList<API>();

    for (IOEndpoint ioEndpoint : emptyIfNull(ioapi.getEndpoints())) {
      API api = new APIImpl();

      api.setBasePath(ioEndpoint.getPath());
      api.setName(ioEndpoint.getName());

      for (IOMethod ioMethod : emptyIfNull(ioEndpoint.getMethods())) {
        /* resource and operation are combined in I/O Docs */

        Resource resource = new ResourceImpl();
        resource.setPath(ioMethod.getURI());

        Operation operation = new OperationImpl();
        operation.setDisplayName(ioMethod.getMethodName());
        operation.setMethod(ioMethod.getHTTPMethod());
        operation.setDocumentation(ioMethod.getSynopsis());
        operation.setResource(resource);

        for (IOParameter ioParameter : emptyIfNull(ioMethod.getParameters())) {
          Parameter parameter = new ParameterImpl();

          parameter.setName(ioParameter.getName());
          parameter.setRequired(ioParameter.isRequired());

          // convert empty string to null
          String def = ioParameter.getDefault();
          parameter.setDefault(def.length() == 0 ? null : def);

          parameter.setDataType(ioParameter.getType());
          parameter.setDocumentation(ioParameter.getDescription());

          for (String enumeratedValue : emptyIfNull(ioParameter.getEnumeratedList())) {
            ValidValue validValue = new ValidValueImpl();
            validValue.setValue(enumeratedValue);
            parameter.addValidValue(validValue);
          }

          operation.addQueryParameter(parameter);
        }

        resource.addOperation(operation);

        api.addResource(resource);
      }

      apis.add(api);
    }

    return apis;
  }

}
