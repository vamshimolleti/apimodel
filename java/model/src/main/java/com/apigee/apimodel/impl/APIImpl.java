package com.apigee.apimodel.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIDifference;
import com.apigee.apimodel.Parameter;
import com.apigee.apimodel.Parameter.Type;
import com.apigee.apimodel.Resource;

@Entity
@Table(name="apis")
public class APIImpl
  extends AbstractDocumentedImpl
  implements API
{
  @Id @GeneratedValue
  private long id;
  
  @Basic @Column(length=128)
  private String name;
  @Basic(optional=true) @Column(length=128)
  private String basePath;
  @Basic(optional=true) @Column(length=128)
  private String onboardingURI;
  
  @OneToMany(targetEntity=ResourceImpl.class, cascade=CascadeType.ALL, mappedBy="parentApi")
  private final List<Resource> resources = new ArrayList<Resource>();
  
  /* All parameters are kept in one big collection. That way, there is one mapping to the
   * "parameters" table in the database using the "parentApi" column. We maintain the various
   * sub-collections in the Java code. That way we use fewer database resources, and the
   * total number of parameters in each case is usally a fairly small size. The other 
   * solutions are to have a parameter subclass for each parameter type, or to have
   * a lot of expensive join tables.
   */
  @OneToMany(targetEntity=ParameterImpl.class, cascade=CascadeType.ALL, mappedBy="parentApi")
  protected final List<Parameter> parameters = new ArrayList<Parameter>();
  
  @Basic(optional=true) @ManyToOne
  private OrganizationImpl parentOrganization;

  private transient boolean normalized = false;
  
  public long getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  @JsonIgnore
  public OrganizationImpl getParentOrganization() {
    return parentOrganization;
  }

  @JsonIgnore
  public void setParentOrganization(OrganizationImpl o) {
    this.parentOrganization = o;
  }
  
  public Collection<Resource> getResources() {
    return Collections.unmodifiableCollection(resources);
  }
  
  @JsonProperty(value="resources")
  public Collection<Map<String, Object>> getSummarizedResources() 
  {
    ArrayList<Map<String, Object>> ret = new  ArrayList<Map<String, Object>>();
    for (Resource r : resources) {
      HashMap<String, Object> m = new HashMap<String, Object>();
      m.put("path", r.getPath());
      m.put("id", ((ResourceImpl)r).getId());
      if (r.getNodeId() != null) {
        m.put("nodeId", r.getNodeId());
      }
      ret.add(m);
    }
    return ret;
  }
  
  public void addResource(Resource r) 
  {
    r.setParentAPI(this);
    resources.add(r);
  }

  public String getBasePath()
  {
    return basePath;
  }

  public void setBasePath(String path)
  {
    this.basePath = path;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getOnboardingURI()
  {
    return onboardingURI;
  }

  public void setOnboardingURI(String uri)
  {
    this.onboardingURI = uri;
  }
  
  @JsonIgnore
  public boolean isHasResponseParameters()
  {
    return (Utils.countParameterTypes(parameters, Type.REQUEST_HEADER) > 0);
  }
  
  @JsonIgnore
  public boolean isHasRequestParameters()
  {
    return (Utils.countParameterTypes(parameters, Type.RESPONSE_HEADER) > 0);
  }
  
  public Map<String, Parameter> getRequestHeaders()
  {
    return Utils.getParamMap(parameters, Type.REQUEST_HEADER);
  }

  public void addRequestHeader(Parameter hdr)
  {
    ((ParameterImpl)hdr).setParentApi(this);
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
    ((ParameterImpl)hdr).setParentApi(this);
    hdr.setType(Type.RESPONSE_HEADER);
    parameters.add(hdr);
  }
  
  @JsonIgnore
  public boolean isNormalized() {
    return normalized; 
  }
  
  public void normalize()
  {
    if (normalized) {
      return;
    }
    
    // Merge nested resources
    for (Resource r : resources) {
      try {
        ((ResourceImpl)r).mergeChildren();
      } catch (ClassCastException cce) {
        throw new AssertionError("Normalization only supported for standard subclasses");
      }
    }
    
    // Now merge all resources and combine all operations
    for (Resource r : resources) {
      try {
        ResourceImpl ri = (ResourceImpl)r;
        ri.mergeAPIParameters(this);
        ri.mergeOperationParameters();
      } catch (ClassCastException cce) {
        throw new AssertionError("Normalization only supported for standard subclasses");
      }
    }
    
    normalized = true;
  }
  
  private HashMap<String, ResourceImpl> buildResourceMap(List<Resource> rl)
  {
    HashMap<String, ResourceImpl> ret = new HashMap<String, ResourceImpl>();
    for (Resource r : rl) {
      ResourceImpl res = (ResourceImpl)r;
      ret.put(res.getPath(), res);
    }
    return ret;
  }

  public List<APIDifference> getDifferences(API api)
  {
    ArrayList<APIDifference> diffs = new ArrayList<APIDifference>();
    if (api instanceof APIImpl) {
      APIImpl a = (APIImpl)api;
      if (!Utils.same(name, a.name)) {
        diffs.add(new APIDifference("/", this, api, "API name does not match"));
      }
      if (!Utils.same(basePath, a.basePath)) {
        diffs.add(new APIDifference("/", this, api, "API base path does not match"));
      }
      if (!Utils.same(onboardingURI, a.onboardingURI)) {
        diffs.add(new APIDifference("/", this, api, "Onboarding URI does not match"));
      }
      
      HashMap<String, ResourceImpl> res1 = buildResourceMap(resources);
      HashMap<String, ResourceImpl> res2 = buildResourceMap(a.resources);
      Utils.compareMaps("resource", "/", res1, res2, diffs);
      
      if (!parameters.equals(a.parameters)) {
        diffs.add(new APIDifference("/", parameters, a.parameters,
                                    "Parameters do not match"));
      }

    } else {
      diffs.add(new APIDifference("/", this, api, "Both objects must be APIImpl objects"));
    }
    return diffs;
  }
  
  public boolean equals(Object o)
  {
    try {
      return getDifferences((API)o).isEmpty();
    } catch (ClassCastException cce) {
      return false;
    }
  }
}
