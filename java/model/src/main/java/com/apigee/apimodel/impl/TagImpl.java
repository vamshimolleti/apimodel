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

import com.apigee.apimodel.Tag;

@Entity
@Table(name="tags")
public class TagImpl 
  implements Tag
{
  @Id @GeneratedValue
  private long id;
  @Basic @Column(length=64)
  private String type;
  @Basic(optional=true) @Column(length=256)
  private String value;
  
  @Basic(optional=true) @ManyToOne
  private OperationImpl parentOperation;
  @Basic(optional=true) @ManyToOne
  private ResourceImpl parentResource;
  
  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getType()
  {
    return type;
  }
  public void setType(String type)
  {
    this.type = type;
  }
  public String getValue()
  {
    return value;
  }
  public void setValue(String value)
  {
    this.value = value;
  }
  public long getId()
  {
    return id;
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
      TagImpl t = (TagImpl)o;
      return (id == t.id) && Utils.same(type, t.type) &&
             Utils.same(value, t.value);
    } catch (ClassCastException cce) {
      return false;
    }
  }
}
