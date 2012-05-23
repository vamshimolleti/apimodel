package com.apigee.apimodel.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
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

import com.apigee.apimodel.APIDifference;
import com.apigee.apimodel.AuthenticationMethod;
import com.apigee.apimodel.Operation;
import com.apigee.apimodel.Parameter;
import com.apigee.apimodel.Parameter.Type;
import com.apigee.apimodel.Resource;
import com.apigee.apimodel.ResponseCode;
import com.apigee.apimodel.Tag;

@Entity
@Table(name="operations")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class OperationImpl 
  extends AbstractRequestResponseImpl
  implements Operation
{
  @Id @GeneratedValue
  private long id;
  
  @Basic(optional=true) @Column(length=128)
  private String displayName;
  
  @ManyToOne(targetEntity=ResourceImpl.class)
  private Resource resource;
  
  @Column(length=64)
  private String method;
  
  @OneToMany(targetEntity=AuthenticationMethodImpl.class, cascade=CascadeType.ALL, mappedBy="parentOperation")
  protected final List<AuthenticationMethod> authMethods = new ArrayList<AuthenticationMethod>();
  
  @OneToMany(cascade=CascadeType.ALL, mappedBy="parentOperation", targetEntity=ParameterImpl.class)
  protected final List<Parameter> parameters = new ArrayList<Parameter>();

  @OneToMany(targetEntity=ResponseCodeImpl.class, cascade=CascadeType.ALL, mappedBy="parentOperation")
  @MapKey(name="code")
  protected final Map<Integer, ResponseCode> responseCodes = new LinkedHashMap<Integer, ResponseCode>();
  
  @OneToMany(targetEntity=TagImpl.class, cascade=CascadeType.ALL, mappedBy="parentOperation")
  protected final List<Tag> tags = new ArrayList<Tag>();
  
  public long getId() {
    return id;
  }
  
  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName(String displayName)
  {
    this.displayName = displayName;
  }

  @JsonIgnore
  public Resource getResource()
  {
    return resource;
  }

  @JsonIgnore
  public void setResource(Resource resource)
  {
    this.resource = resource;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod(String method)
  {
    this.method = method;
  }
  
  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public Collection<Tag> getTags()
  {
    return Collections.unmodifiableCollection(tags);
  }
  
  public void addTag(Tag tag)
  {
    ((TagImpl)tag).setParentOperation(this);
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
    ((ResponseCodeImpl)code).setParentOperation(this);
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
    ((ParameterImpl)qp).setParentOperation(this);
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
    ((AuthenticationMethodImpl)method).setParentOperation(this);
    authMethods.add(method);
  }
  
  @JsonIgnore
  public boolean isHasResponseParameters()
  {
    return (Utils.countParameterTypes(parameters, Type.RESPONSE_HEADER) > 0);
  }
  
  @JsonIgnore
  public boolean isHasRequestParameters()
  {
    return (Utils.countParameterTypes(parameters, Type.QUERY) > 0) ||
           (Utils.countParameterTypes(parameters, Type.REQUEST_HEADER) > 0);
  }
  
  public Map<String, Parameter> getRequestHeaders()
  {
    return Utils.getParamMap(parameters, Type.REQUEST_HEADER);
  }

  public void addRequestHeader(Parameter hdr)
  {
    ((ParameterImpl)hdr).setParentOperation(this);
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
    ((ParameterImpl)hdr).setParentOperation(this);
    hdr.setType(Type.RESPONSE_HEADER);
    parameters.add(hdr);
  }
  
  void mergeParameters(ResourceImpl r)
  {
    authMethods.addAll(0, r.getAuthenticationMethods());
    tags.addAll(0, r.getTags());
    Utils.mergeParams(parameters, r.parameters);
    Utils.mergeInto(responseCodes, r.responseCodes);
  }

  void addDiffs(OperationImpl o, String path, List<APIDifference> diffs)
  {
    if (id != o.id) {
      diffs.add(new APIDifference(path, this, o, "ID does not match"));
    }
    if (!Utils.same(displayName, o.displayName)) {
      diffs.add(new APIDifference(path, this, o, "Display name does not match"));
    }
    if (!Utils.same(method, o.method)) {
      diffs.add(new APIDifference(path, this, o, "Method does not match"));
    }
    if (!authMethods.equals(o.authMethods)) {
      diffs.add(new APIDifference(path, authMethods, o.authMethods, "Authentication methods differ"));
    }
    if (!responseCodes.equals(o.responseCodes)) {
      Utils.compareMaps("response code", path, responseCodes, o.responseCodes, diffs);
    }
    if (!tags.equals(o.tags)) {
      diffs.add(new APIDifference(path, tags, o.tags, "Tags differ"));
    }
    if (!parameters.equals(o.parameters)) {
      diffs.add(new APIDifference(path + "/", parameters, o.parameters,
                                  "Parameters do not match"));
    }
    addRRDiffs(o, path, diffs);
  }
}
