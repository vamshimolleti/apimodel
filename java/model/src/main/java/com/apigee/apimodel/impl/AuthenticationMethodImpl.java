package com.apigee.apimodel.impl;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.apigee.apimodel.AuthenticationMethod;

@Entity
@Table(name="authenticationmethods")
public class AuthenticationMethodImpl 
  extends AbstractDocumentedImpl
  implements AuthenticationMethod
{
  @Id @GeneratedValue
  private long id;
  @Basic @Column(length=64)
  private String name;
  @Basic(optional=true) @Column(length=4096)
  private String description;
  
  @Basic(optional=true) @ManyToOne
  private OperationImpl parentOperation;
  @Basic(optional=true) @ManyToOne
  private ResourceImpl parentResource;
  
  public long getId() {
    return id;
  }
  
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getDescription()
  {
    return description;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public void setDescription(String desc)
  {
    this.description = desc;
  }
  
  @JsonIgnore
  public OperationImpl getParentOperation()
  {
    return parentOperation;
  }

  @JsonIgnore
  public void setParentOperation(OperationImpl parentOperation)
  {
    this.parentOperation = parentOperation;
  }

  @JsonIgnore
  public ResourceImpl getParentResource()
  {
    return parentResource;
  }

  @JsonIgnore
  public void setParentResource(ResourceImpl parentResource)
  {
    this.parentResource = parentResource;
  }

  public boolean equals(Object o)
  {
    try {
      AuthenticationMethodImpl m = (AuthenticationMethodImpl)o;
      return Utils.same(name, m.name) && Utils.same(description, m.description);
    } catch (ClassCastException cce) {
      return false;
    }
  }
}
