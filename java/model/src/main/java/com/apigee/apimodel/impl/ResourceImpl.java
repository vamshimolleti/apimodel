package com.apigee.apimodel.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIDifference;
import com.apigee.apimodel.AuthenticationMethod;
import com.apigee.apimodel.Operation;
import com.apigee.apimodel.Parameter;
import com.apigee.apimodel.Parameter.Type;
import com.apigee.apimodel.Resource;
import com.apigee.apimodel.ResponseCode;
import com.apigee.apimodel.Tag;

@Entity
@Table(name="resources")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class ResourceImpl 
  extends AbstractRequestResponseImpl
  implements Resource
{
  @Id @GeneratedValue
  private long id;
  
  @Basic @Column(length=256)
  private String path;
  
  @JsonIgnore
  @ManyToOne(targetEntity=APIImpl.class, cascade=CascadeType.ALL)
  private API parentApi;
  
  @JsonIgnore
  @ManyToOne(targetEntity=ResourceImpl.class, cascade=CascadeType.ALL)
  private Resource parentResource;
  
  @OneToMany(targetEntity=ResourceImpl.class, cascade=CascadeType.ALL, mappedBy="parentResource")
  private final List<Resource> childResources = new ArrayList<Resource>();
  
  @OneToMany(targetEntity=ParameterImpl.class, cascade=CascadeType.ALL, mappedBy="parentResource") 
  protected final List<Parameter> parameters = new ArrayList<Parameter>();
   
  @OneToMany(targetEntity=OperationImpl.class, mappedBy="resource", cascade=CascadeType.ALL) 
  protected final List<Operation> operations = new ArrayList<Operation>();
  
  @OneToMany(targetEntity=AuthenticationMethodImpl.class, cascade=CascadeType.ALL, mappedBy="parentResource")
  protected final List<AuthenticationMethod> authMethods = new ArrayList<AuthenticationMethod>();
   
  @OneToMany(targetEntity=ResponseCodeImpl.class, cascade=CascadeType.ALL, mappedBy="parentResource")
  @MapKey(name="code")
  protected final Map<Integer, ResponseCode> responseCodes = new HashMap<Integer, ResponseCode>();
  
  @OneToMany(targetEntity=TagImpl.class, cascade=CascadeType.ALL, mappedBy="parentResource")
  protected final List<Tag> tags = new ArrayList<Tag>();
    
  public long getId() {
    return id;
  }

  @JsonIgnore
  public API getParentAPI() {
    return parentApi;
  }
  
  @JsonIgnore
  public void setParentAPI(API a) {
    this.parentApi = a;
  }
  
  @JsonIgnore
  public boolean isHasRequestParameters()
  {
    return (Utils.countParameterTypes(parameters, Type.QUERY) > 0) ||
           (Utils.countParameterTypes(parameters, Type.MATRIX) > 0) ||
           (Utils.countParameterTypes(parameters, Type.TEMPLATE) > 0) ||
           (Utils.countParameterTypes(parameters, Type.REQUEST_HEADER) > 0);
  }
  
  public String getPath()
  {
    return path;
  }

  public void setPath(String path)
  {
    this.path = path;
  }
  
  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public List<Parameter> getTemplateParameters()
  {
    return Utils.getParamCollection(parameters, Type.TEMPLATE);
  }

  public void addTemplateParameter(Parameter parameter)
  {
    ((ParameterImpl)parameter).setParentResource(this);
    parameter.setType(Type.TEMPLATE);
    parameters.add(parameter);
  }
  
  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public List<Parameter> getMatrixParameters()
  {
    return Utils.getParamCollection(parameters, Type.MATRIX);
  }

  public void addMatrixParameter(Parameter parameter)
  {
    ((ParameterImpl)parameter).setParentResource(this);
    parameter.setType(Type.MATRIX);
    parameters.add(parameter);
  }

  @JsonIgnore
  public Resource getParentResource()
  {
    return parentResource;
  }

  @JsonIgnore
  public void setParentResource(Resource res)
  {
    this.parentResource = res;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public Collection<Resource> getChildResources()
  {
    return Collections.unmodifiableCollection(childResources);
  }

  public void addChildResource(Resource res)
  {
    childResources.add(res);
  }
  
  public Collection<Operation> getOperations()
  {
    return Collections.unmodifiableCollection(operations);
  }
  
  @JsonProperty(value="operations")
  public Collection<Map<String, Object>> getSummarizedOperations()
  {
    ArrayList<Map<String, Object>> a = new ArrayList<Map<String, Object>>();
    for (Operation o : operations) {
      HashMap<String, Object> m = new HashMap<String, Object>();
      m.put("method", o.getMethod());
      if (o.getNodeId() != null) {
        m.put("nodeId", o.getNodeId());
      }
      a.add(m);
    }
    return a;
  }

  public void addOperation(Operation op)
  {
    op.setResource(this);
    operations.add(op);
  }
  
  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public Collection<Tag> getTags()
  {
    return Collections.unmodifiableCollection(tags);
  }
  
  public void addTag(Tag tag)
  {
    ((TagImpl)tag).setParentResource(this);
    tags.add(tag);
  }

  public Map<Integer, ResponseCode> getResponseCodes()
  {
    return Collections.unmodifiableMap(responseCodes);
  }

  @JsonProperty(value="responseCodes") @JsonSerialize(include=Inclusion.NON_EMPTY)
  public Collection<ResponseCode> getResponseCodeValues()
  {
    return Collections.unmodifiableCollection(responseCodes.values());
  }

  public void addResponseCode(ResponseCode code)
  {
    ((ResponseCodeImpl)code).setParentResource(this);
    responseCodes.put(code.getCode(), code);
  }

  public Map<String, Parameter> getQueryParameters()
  {
    return Utils.getParamMap(parameters, Type.QUERY);
  }
  
  @JsonProperty(value="queryParameters") @JsonSerialize(include=Inclusion.NON_EMPTY)
  public Collection<Parameter> getQueryParamValues()
  {  
    return Utils.getParamCollection(parameters, Type.QUERY);
  }
  
  public void addQueryParameter(Parameter qp)
  {
    ((ParameterImpl)qp).setParentResource(this);
    qp.setType(Type.QUERY);
    parameters.add(qp);
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public Collection<AuthenticationMethod> getAuthenticationMethods()
  {
    return Collections.unmodifiableCollection(authMethods);
  }

  public void addAuthenticationMethod(AuthenticationMethod method)
  {
    ((AuthenticationMethodImpl)method).setParentResource(this);
    authMethods.add(method);
  }
  
  @JsonIgnore
  public boolean isHasResponseParameters()
  {
    return (Utils.countParameterTypes(parameters, Type.RESPONSE_HEADER) > 0);
  }
  
  public Map<String, Parameter> getRequestHeaders()
  {
    return Utils.getParamMap(parameters, Type.REQUEST_HEADER);
  }

  public void addRequestHeader(Parameter hdr)
  {
    ((ParameterImpl)hdr).setParentResource(this);
    hdr.setType(Type.REQUEST_HEADER);
    parameters.add(hdr);
  }
  @JsonProperty(value="requestHeaders") @JsonSerialize(include=Inclusion.NON_EMPTY)
  public Collection<Parameter> getRequestHeaderValues()
  {
    return Utils.getParamCollection(parameters, Type.REQUEST_HEADER);
  }
  
  public Map<String, Parameter> getResponseHeaders()
  {
    return Utils.getParamMap(parameters, Type.RESPONSE_HEADER);
  }

  @JsonProperty(value="responseHeaders") @JsonSerialize(include=Inclusion.NON_EMPTY)
  public Collection<Parameter> getResponseHeaderValues()
  {
    return Utils.getParamCollection(parameters, Type.RESPONSE_HEADER);
  }

  public void addResponseHeader(Parameter hdr)
  {
    ((ParameterImpl)hdr).setParentResource(this);
    hdr.setType(Type.RESPONSE_HEADER);
    parameters.add(hdr);
  }
  
  void mergeChildren()
  {
    Iterator<Resource> i = childResources.iterator();
    while (i.hasNext()) {
      // CCE caught by caller
      ResourceImpl r = (ResourceImpl)i.next();
      r.mergeResourceParams(this);
      r.setPath(path + '/' + r.getPath());
      r.setParentResource(null);
      i.remove();
      parentApi.addResource(r);
      r.mergeChildren();
    }
  }
  

  private void mergeResourceParams(ResourceImpl r)
  {
    Utils.mergeNestedParams(parameters, r.parameters);
  }
  
  void mergeAPIParameters(APIImpl a)
  {
    Utils.mergeParams(parameters, a.parameters);
  }
  
  void mergeOperationParameters()
  {
    for (Operation o : operations) {
      // CCE caught by caller
      ((OperationImpl)o).mergeParameters(this);
    }
  }
  
  private HashMap<String, OperationImpl> buildOpMap(List<Operation> os) 
  {
    HashMap<String, OperationImpl> ret = new HashMap<String, OperationImpl>();
    for (Operation o : os) {
      OperationImpl oi = (OperationImpl)o;
      ret.put(oi.getMethod(), oi);
    }
    return ret;
  }
  
  void addDiffs(ResourceImpl r, String p, List<APIDifference> diffs)
  {
    if (id != r.id) {
      diffs.add(new APIDifference(path, this, r, "IDs do not match"));
    }
    if (!childResources.equals(r.childResources)) {
      diffs.add(new APIDifference(path, this, r, "Child resources do not match"));
    }
    if (!parameters.equals(r.parameters)) {
      diffs.add(new APIDifference(path, this, r, "Parameters do not match"));
    }
    if (!Utils.same(path, r.path)) {
      diffs.add(new APIDifference(path, this, r, "Path does not match: \"" + path + "\" != \"" +
                                  r.path + '\"'));
    }
    if (!authMethods.equals(r.authMethods)) {
      diffs.add(new APIDifference(path, authMethods, r.authMethods, "Authentication methods differ"));
    }
    if (!responseCodes.equals(r.responseCodes)) {
      Utils.compareMaps("response code", path, responseCodes, r.responseCodes, diffs);
    }
    if (!tags.equals(r.tags)) {
      diffs.add(new APIDifference(path, tags, r.tags, "Tags differ"));
    }
    addRRDiffs(r, path, diffs);
     
    HashMap<String, OperationImpl> ops1 = buildOpMap(operations);
    HashMap<String, OperationImpl> ops2 = buildOpMap(r.operations);
    Utils.compareMaps("operation", p, ops1, ops2, diffs);
  }
}
