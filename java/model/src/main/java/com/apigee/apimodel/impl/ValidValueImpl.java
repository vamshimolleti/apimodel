package com.apigee.apimodel.impl;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.apigee.apimodel.Parameter;
import com.apigee.apimodel.ValidValue;

@Entity
@Table(name="validvalues")
public class ValidValueImpl
  extends AbstractDocumentedImpl
  implements ValidValue
{
  @Id @GeneratedValue
  private long id;
  @Basic @ManyToOne(cascade=CascadeType.ALL, targetEntity=ParameterImpl.class)
  private Parameter parameter;
  @Column(length=128)
  private String value;
  @Basic(optional=true) @Column(length=128)
  private String mediaType;

  public long getId() {
    return id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  @JsonIgnore
  public Parameter getParameter() {
    return parameter;
  }
  
  @JsonIgnore
  public void setParameter(Parameter p) {
    this.parameter = p;
  }
  
  public String getValue()
  {
    return value;
  }

  public void setValue(String param)
  {
    this.value = param;
  }
  
  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getMediaType()
  {
    return mediaType;
  }

  public void setMediaType(String mediaType)
  {
    this.mediaType = mediaType;
  }
  
  public boolean equals(Object o)
  {
    try {
      ValidValueImpl vv = (ValidValueImpl)o;
      return (id == vv.id) && Utils.same(value, vv.value) && Utils.same(mediaType, vv.mediaType);
    } catch (ClassCastException cce) {
      return false;
    }
  }
  
  public String toString()
  {
    StringBuilder s = new StringBuilder();
    s.append(value);
    if (mediaType != null) {
      s.append(" (");
      s.append(mediaType);
      s.append(')');
    }
    return s.toString();
  }
}
