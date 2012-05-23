package com.apigee.apimodel.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;

import com.apigee.apimodel.API;
import com.apigee.apimodel.Organization;

@Entity
@Table(name="organizations")
public class OrganizationImpl
  implements Organization
{
  @Basic @Id
  private String name;
  @OneToMany(cascade=CascadeType.ALL,targetEntity=APIImpl.class, mappedBy="parentOrganization") 
  private final List<API> apis = new ArrayList<API>();
  
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Collection<API> getApis()
  {
    return Collections.unmodifiableCollection(apis);
  }

  public void addApi(API a)
  {
    ((APIImpl)a).setParentOrganization(this);
    apis.add(a);
  }
  
  public API deleteApi(String name)
  {
    Iterator<API> i = apis.iterator();
    while (i.hasNext()) {
      API a = i.next();
      if (a.getName().equals(name)) {
        i.remove();
        return a;
      }
    }
    return null;
  }
  
  @JsonProperty(value="apis")
  public List<String> getSummarizedAPIs()
  {
    ArrayList<String> ret = new ArrayList<String>();
    for (API a : apis) {
      ret.add(a.getName());
    }
    return ret;
  }
}
