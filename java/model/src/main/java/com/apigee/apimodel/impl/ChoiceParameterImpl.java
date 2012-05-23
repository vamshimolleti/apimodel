package com.apigee.apimodel.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.apigee.apimodel.ChoiceParameter;
import com.apigee.apimodel.Parameter;

@Entity
@DiscriminatorValue(value="1")
public class ChoiceParameterImpl 
  extends ParameterImpl
  implements ChoiceParameter
{
  @Basic(optional=true) @Column(length=16)
  private int minCount;
  @Basic(optional=true) @Column(length=16)
  private int maxCount;
  
  @OneToMany(targetEntity=ParameterImpl.class, cascade=CascadeType.ALL, mappedBy="parentParameter")
  private final List<Parameter> choices = new ArrayList<Parameter>();
  
  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public Collection<Parameter> getChoices()
  {
    return Collections.unmodifiableCollection(choices);
  }
  
  public void addChoice(Parameter p)
  {
    ((ParameterImpl)p).setParentParameter(this);
    choices.add(p);
  }

  public int getMinCount()
  {
    return minCount;
  }

  public void setMinCount(int minCount)
  {
    this.minCount = minCount;
  }

  public int getMaxCount()
  {
    return maxCount;
  }

  public void setMaxCount(int maxCount)
  {
    this.maxCount = maxCount;
  }
  
  public boolean equals(Object o)
  {
    try {
      ChoiceParameterImpl p = (ChoiceParameterImpl)o;
      if (!choices.equals(p.choices)) {
        return false;
      }
      if ((maxCount != p.maxCount) || (minCount != p.minCount)) {
        return false;
      }
      return super.equals(o);
    } catch (ClassCastException cce) {
      return false;
    }
  }
}
