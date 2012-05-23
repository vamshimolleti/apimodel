package com.apigee.apimodel.impl;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.apigee.apimodel.APIDifference;
import com.apigee.apimodel.Example;
import com.apigee.apimodel.Representation;
import com.apigee.apimodel.RequestParameters;
import com.apigee.apimodel.ResponseParameters;

@MappedSuperclass
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractRequestResponseImpl 
  extends AbstractDocumentedImpl
  implements RequestParameters, ResponseParameters
{
  @Basic(optional=true) @OneToOne(targetEntity=RepresentationImpl.class, cascade=CascadeType.ALL)
  protected Representation requestRepresentation;
  @Basic(optional=true) @OneToOne(targetEntity=RepresentationImpl.class, cascade=CascadeType.ALL)
  protected Representation responseRepresentation;
  @Basic(optional=true) @OneToOne(targetEntity=ExampleImpl.class, cascade=CascadeType.ALL)
  protected Example exampleRequest;
  @Basic(optional=true) @OneToOne(targetEntity=ExampleImpl.class, cascade=CascadeType.ALL)
  protected Example exampleResponse;
  @Basic(optional=true) @Column(length=256)
  protected String exampleUri;

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getExampleURI()
  {
    return exampleUri;
  }

  public void setExampleURI(String uri)
  {
    this.exampleUri = uri;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public Representation getRequestRepresentation()
  {
    return requestRepresentation;
  }

  public void setRequestRepresentation(Representation rep)
  {
    this.requestRepresentation = rep;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public Example getExampleRequest()
  {
    return exampleRequest;
  }

  public void setExampleRequest(Example example)
  {
    this.exampleRequest = example;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public Representation getResponseRepresentation()
  {
    return responseRepresentation;
  }

  public void setResponseRepresentation(Representation rep)
  {
    this.responseRepresentation = rep;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public Example getExampleResponse()
  {
    return exampleResponse;
  }

  public void setExampleResponse(Example example)
  {
    this.exampleResponse = example;
  }
  
  void addRRDiffs(AbstractRequestResponseImpl r, String path, List<APIDifference> diffs)
  {
    if (!Utils.same(exampleRequest, r.exampleRequest)) {
      diffs.add(new APIDifference(path, exampleRequest, r.exampleRequest, "Example request differs"));
    }
    if (!Utils.same(exampleResponse, r.exampleResponse)) {
      diffs.add(new APIDifference(path, exampleResponse, r.exampleResponse, "Example response differs"));
    }
    if (!Utils.same(exampleUri, r.exampleUri)) {
      diffs.add(new APIDifference(path, exampleUri, r.exampleUri, "Example URI differs"));
    }
    if (!Utils.same(requestRepresentation, r.requestRepresentation)) {
      diffs.add(new APIDifference(path, requestRepresentation, r.requestRepresentation, "Request representation differs"));
    }
    if (!Utils.same(responseRepresentation, r.responseRepresentation)) {
      diffs.add(new APIDifference(path, responseRepresentation, r.responseRepresentation, "Response representation differs"));
    }
  }
}
