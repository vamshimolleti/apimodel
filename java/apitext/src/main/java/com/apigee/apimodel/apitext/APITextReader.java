package com.apigee.apimodel.apitext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIModelException;
import com.apigee.apimodel.APIReader;
import com.apigee.apimodel.Documented;
import com.apigee.apimodel.Operation;
import com.apigee.apimodel.Parameter;
import com.apigee.apimodel.RequestParameters;
import com.apigee.apimodel.Resource;
import com.apigee.apimodel.impl.APIImpl;
import com.apigee.apimodel.impl.OperationImpl;
import com.apigee.apimodel.impl.ParameterImpl;
import com.apigee.apimodel.impl.RepresentationImpl;
import com.apigee.apimodel.impl.ResourceImpl;
import com.apigee.apimodel.impl.ResponseCodeImpl;
import com.apigee.apimodel.impl.ValidValueImpl;

public class APITextReader 
  implements APIReader
{
  /** Starting with either four spaces or a tab, per Markdown */
  private static final Pattern reCodeLine = Pattern.compile("^    \\s*[^\\s].*$|^\\t\\s*[^\\s].*$");
  /** "    variable = value" */
  private static final Pattern reAssignment = Pattern.compile("^\\s+([^\\s]+)\\s*=\\s*(.*)$");
  /** "    resource URL" */
  private static final Pattern reResource = Pattern.compile("^\\s+resource\\s+(.+)$");
  /** "    operation VERB" */
  private static final Pattern reOperation = Pattern.compile("^\\s+operation\\s+([a-zA-Z]+)\\s*$");
  /** "    request" */
  private static final Pattern reRequest = Pattern.compile("^\\s+request\\s*$");
  /** "    request" */
  private static final Pattern reResponse = Pattern.compile("^\\s+response\\s*$");
  /** "    type name (params)" */
  private static final Pattern reParams = Pattern.compile("^\\s+([^\\s]+)\\s+([^\\s]+)\\s+\\(([^)]*)\\)\\s*$");
  /** "    valid value (mediaType)" */
  private static final Pattern reValid = 
    Pattern.compile("^\\s+valid\\s+([^\\s]+)\\s+\\(([^)]*)\\)\\s*$|^\\s+valid\\s+([^\\s]+)\\s*");
  /** 'default=foobar' and 'default="foobar"'for the parameters */
  private static final Pattern reDefault = Pattern.compile("default\\s*=\\s*\"([^\"]+)\"$|default\\s*=\\s*([^\\s,]+)$");
  
  private static final APITextReader myself = new APITextReader();
  
  private enum State { IDLE, RESOURCE, RESOURCE_REQUEST, OPERATION, REQUEST, RESPONSE, 
                       RREQ_VALID, REQ_VALID, RESP_VALID };

  
  private static final boolean DEBUG = false;
  
  private APITextReader()
  {
  }
  
  public static final APITextReader get() {
    return myself;
  }
  
  public List<API> readModel(String name, InputStream in, Logger log)
      throws APIModelException, IOException
  {
    Rdr r = new Rdr();
    return r.readModel(name, in, log);
  }
  
  public List<API> readModel(String name, InputStream in) throws APIModelException,
    IOException
  {
    return readModel(name, in, Logger.getLogger(APIReader.DEFAULT_READER_LOGGER));
  }
  
  private static class Rdr
  {
    private final HashMap<String, String> variables = new HashMap<String, String>();
    private final ArrayList<API> models = new ArrayList<API>();
    private API curModel = null;
    private Resource curResource = null;
    private Operation curOperation = null;
    private Parameter curParam = null;
    private String basePath = null;
    private StringBuilder docs = new StringBuilder();
    
    List<API> readModel(String name, InputStream in, Logger log)
        throws APIModelException, IOException
    {
      BufferedReader reader =
        new BufferedReader(new InputStreamReader(in));
      int lineNo = 1;
      String line;
      State state = State.IDLE;
      
      variables.clear();
      models.clear();
      
      while ((line = reader.readLine()) != null) {
        Matcher m = reCodeLine.matcher(line);
        if (m.matches()) {
          if (DEBUG) {
            System.err.println(line);
          }
          state = runMachine(name, state, line, lineNo, docs);
          if (DEBUG) {
            System.err.println("  <-- " + state);
          }
        } else {
          docs.append(line);
        }
        lineNo++;
      }
      
      return models;
    }
    
    private State runMachine(String name, State s, String line, int lineNo, StringBuilder docs) 
        throws APIModelException
    {
      State state = s;
      String pType;
      String pName;
      Matcher m;

      while (true) {
        if (DEBUG) {
          System.err.println("-->   " + state);
        }
        switch (state) {
        case IDLE:
          Matcher rem = reAssignment.matcher(line);
          if (rem.matches()) {
            if (rem.group(1).equalsIgnoreCase("basepath")) {
              basePath = rem.group(2);
              curModel = new APIImpl();
              curModel.setName(name);
              curModel.setBasePath(basePath);
              collectDocs(curModel);
              models.add(curModel);
            } else {
              // TODO handle the "name" variable properly
              variables.put(rem.group(1), rem.group(2));
            }
            return State.IDLE;
          }

          Matcher resm = reResource.matcher(line);
          if (resm.matches()) {
            String resourceUri = resm.group(1);
            curResource = new ResourceImpl();
            curResource.setPath(resourceUri);
            curModel.addResource(curResource);
            collectDocs(curResource);
            return State.RESOURCE;
          }
          return State.IDLE;

        case RESOURCE:
          m = reOperation.matcher(line);
          if (m.matches()) {
            String opMethod = m.group(1);
            curOperation = new OperationImpl();
            curOperation.setMethod(opMethod);
            curOperation.setResource(curResource);
            curResource.addOperation(curOperation);
            collectDocs(curOperation);
            return State.OPERATION;
          } else if (reRequest.matcher(line).matches()) {
            return State.RESOURCE_REQUEST;
          }
          state = State.IDLE;
          break;

        case OPERATION:
          if (reRequest.matcher(line).matches()) {
            return State.REQUEST;
          } else if (reResponse.matcher(line).matches()) {
            return State.RESPONSE;
          }
          state = State.RESOURCE;
          break;

        case RESOURCE_REQUEST:
          m = reParams.matcher(line);
          if (m.matches()) {
            pType = m.group(1);
            pName = m.group(2);
            curParam = makeParam(pName, m.group(3));
            if (pType.equalsIgnoreCase("templateParam")) {
              curResource.addTemplateParameter(curParam);
            } else if (pType.equalsIgnoreCase("matrixParam")) {
              curResource.addMatrixParameter(curParam);
            } else if (!addRequestParams(curResource, pType, curParam)) {
              throw new APIModelException(lineNo + ": Invalid parameter type " + m.group(1));
            }
            return State.RREQ_VALID;
          }
          state = State.RESOURCE;
          break;

        case RREQ_VALID:
          m = reValid.matcher(line);
          if (m.matches()) {
            setValid(curParam, m);
            return State.RREQ_VALID;
          }
          state = State.RESOURCE_REQUEST;
          break;

        case REQUEST:
          m = reParams.matcher(line);
          if (m.matches()) {
            pType = m.group(1);
            pName = m.group(2);
            if (pType.equalsIgnoreCase("requires")) {
              RepresentationImpl repr = new RepresentationImpl();
              repr.setMediaType(pName);
              // TODO the rest
              curOperation.setRequestRepresentation(repr);
            } else {
              curParam = makeParam(pName, m.group(3));
              if (!addRequestParams(curOperation, pType, curParam)) {
                throw new APIModelException(lineNo + ": Invalid parameter type " + pType);
              }
              return State.REQ_VALID;
            }
            return State.REQUEST;
          }
          state = State.OPERATION;
          break;

        case REQ_VALID:
          m = reValid.matcher(line);
          if (m.matches()) {
            setValid(curParam, m);
            return State.REQ_VALID;
          }
          state = State.REQUEST;
          break;

        case RESPONSE:
          m = reParams.matcher(line);
          if (m.matches()) {
            pType = m.group(1);
            pName = m.group(2);
            if (pType.equalsIgnoreCase("responseHeader")) {
              curParam = makeParam(pName, m.group(3));
              curOperation.addResponseHeader(curParam);
              return State.RESP_VALID;
            } else if (pType.equalsIgnoreCase("code")) {
              ResponseCodeImpl code = new ResponseCodeImpl();
              code.setCode(Integer.parseInt(pName));
              // TODO success
              curOperation.addResponseCode(code);
            } else if (pType.equalsIgnoreCase("returns")) {
              RepresentationImpl repr = new RepresentationImpl();
              repr.setMediaType(pName);
              // TODO other fields
              curOperation.setResponseRepresentation(repr);
            } else {
              throw new APIModelException(lineNo + ": Invalid parameter type " + pType);
            }
            return State.RESPONSE;
          }
          state = State.OPERATION;
          break;

        case RESP_VALID:
          m = reValid.matcher(line);
          if (m.matches()) {
            setValid(curParam, m);
            return State.RESP_VALID;
          }
          state = State.RESPONSE;
          break;

        default:
          throw new APIModelException("line " + lineNo + ": internal error");
        }
      }
    }
    
    private void collectDocs(Documented t)
    {
      t.setDocumentation(docs.toString());
      docs = new StringBuilder();
    }

    private boolean addRequestParams(RequestParameters t, String type, Parameter p)
    {
      if (type.equalsIgnoreCase("queryparam")) {
        t.addQueryParameter(p);
      } else if (type.equalsIgnoreCase("requestheader")) {
        t.addRequestHeader(p);
      } else {
        return false;
      }
      return true;
    }

    private void setValid(Parameter p, Matcher m)
    {
      ValidValueImpl vv = new ValidValueImpl();
      vv.setValue(m.group(1));
      if (m.group(1) == null) {
        vv.setValue(m.group(3));
      } else if (m.group(2) != null) {
        vv.setMediaType(m.group(2));
      }
      p.addValidValue(vv);
    }

    private Parameter makeParam(String name, String stuff)
    {
      Matcher m;
      ParameterImpl p = new ParameterImpl();

      p.setName(name);
      String[] ss = stuff.split(",");
      if (ss.length > 0) {
        p.setDataType(ss[0].trim());
      }
      for (int i = 1; i < ss.length; i++) {
        String v = ss[i].trim();
        if (v.equalsIgnoreCase("optional")) {
          p.setRequired(false);
        } else if (v.equalsIgnoreCase("required")) {
          p.setRequired(true);
        } else if ((m = reDefault.matcher(v)).matches()) {
          if (m.group(1) == null) {
            p.setDefault(m.group(2));
          } else {
            p.setDefault(m.group(1));
          }
        } else if (v.equalsIgnoreCase("repeating")) {
          p.setRepeating(true);
        }
      }
      collectDocs(p);
      return p;
    }
  }
}
