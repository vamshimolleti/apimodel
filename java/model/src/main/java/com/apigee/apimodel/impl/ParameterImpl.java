package com.apigee.apimodel.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.apigee.apimodel.ValidValue;
import com.apigee.apimodel.Parameter;

@Entity
@Table(name="parameters")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.INTEGER, name="pType")
@DiscriminatorValue(value="0")
public class ParameterImpl 
  extends AbstractDocumentedImpl
  implements Parameter
{
  @Id @GeneratedValue(strategy=GenerationType.AUTO)
  private long paramId;
  @Basic @Column(length=128)
  private String name;
  @Basic
  private int required; // This is an int because Hibernate couldn't deal with boolean
  @Basic
  private int repeating;
  @Enumerated(EnumType.STRING)
  private Type type;
  @Basic(optional=true) @Column(length=64)
  private String dataType;
  @Basic(optional=true) @Column(length=256)
  private String deflt;
  
  @Basic(optional=true) @ManyToOne
  private APIImpl parentApi;
  @Basic(optional=true) @ManyToOne
  private ResourceImpl parentResource;
  @Basic(optional=true) @ManyToOne
  private OperationImpl parentOperation;
  @Basic(optional=true) @ManyToOne
  private ParameterImpl parentParameter;
  
  @OneToMany(targetEntity=ValidValueImpl.class, cascade=CascadeType.ALL, mappedBy="parameter")
  private final List<ValidValue> validValues;
  
  public ParameterImpl()
  {
    validValues = new ArrayList<ValidValue>();
  }
  
  public ParameterImpl(Parameter p)
  {
    setDataType(p.getDataType());
    setDefault(p.getDefault());
    setName(p.getName());
    setRepeating(p.isRepeating());
    setRequired(p.isRequired());
    this.validValues = new ArrayList<ValidValue>(p.getValidValues());
    if (p instanceof ParameterImpl) {
      this.paramId = ((ParameterImpl)p).paramId;
    }
  }
  
  public long getParamId() {
    return paramId;
  }
  public void setParamId(long id) {
    this.paramId = id;
  }
  
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public boolean isRequired()
  {
    return (required != 0);
  }

  public void setRequired(boolean required)
  {
    this.required = required ? 1 : 0;
  }

  public boolean isRepeating()
  {
    return (repeating != 0);
  }

  public void setRepeating(boolean repeating)
  {
    this.repeating = repeating ? 1 : 0;
  }

  public Type getType()
  {
    return type;
  }

  public void setType(Type type)
  {
    this.type = type;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getDefault()
  {
    return deflt;
  }

  public void setDefault(String def)
  {
    this.deflt = def;
  }
  
  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public Collection<ValidValue> getValidValues()
  {
    return Collections.unmodifiableCollection(validValues);
  }
  
  public void addValidValue(ValidValue choice)
  {
    ((ValidValueImpl)choice).setParameter(this);
    validValues.add(choice);
  }
  
  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getDataType()
  {
    return dataType;
  }

  public void setDataType(String dataType)
  {
    this.dataType = dataType;
  }
  
  @JsonIgnore
  public APIImpl getParentApi()
  {
    return parentApi;
  }

  @JsonIgnore
  public void setParentApi(APIImpl parentApi)
  {
    this.parentApi = parentApi;
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
  public ParameterImpl getParentParameter()
  {
    return parentParameter;
  }

  @JsonIgnore
  public void setParentParameter(ParameterImpl parentParameter)
  {
    this.parentParameter = parentParameter;
  }

  public boolean equals(Object o)
  {
    try {
      ParameterImpl p = (ParameterImpl)o;
      if (!Utils.same(name, p.name)) {
        return false;
      }
      if (!Utils.same(dataType, p.dataType)) {
        return false;
      }
      if (!Utils.same(deflt, p.deflt)) {
        return false;
      }
      return ((repeating == p.repeating) &&
              (required == p.required) &&
              (type == p.type) && (validValues.equals(p.validValues)));
      
    } catch (ClassCastException cce) {
      return false;
    }
  }
}
