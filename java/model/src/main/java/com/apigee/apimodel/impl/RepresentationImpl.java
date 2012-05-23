package com.apigee.apimodel.impl;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.apigee.apimodel.Representation;

@Entity
@Table(name="representations")
public class RepresentationImpl 
  extends AbstractDocumentedImpl
  implements Representation
{
  @Id @GeneratedValue
  private long id;
  @Basic @Column(length=64)
  private String mediaType;
  @Basic @Column(length=64)
  private String type;
  @Basic(optional=true) @Column(length=4096)
  private String definition;
  @Basic(optional=true) @Column(length=512)
  private String link;
  
  public long getId() {
    return id;
  }
  
  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getMediaType()
  {
    return mediaType;
  }

  public void setMediaType(String type)
  {
    this.mediaType = type;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getDefinition()
  {
    return definition;
  }

  public void setDefinition(String definition)
  {
    this.definition = definition;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getLink()
  {
    return link;
  }

  public void setLink(String link)
  {
    this.link = link;
  }
  
  public boolean equals(Object o)
  {
    try {
      RepresentationImpl re = (RepresentationImpl)o;
      if ((id != re.id) || !Utils.same(mediaType, re.mediaType) ||
          !Utils.same(type, re.type) || !Utils.same(definition, re.definition) ||
          !Utils.same(link, re.link)) {
        return false;
      }
      return super.equals(o);
    } catch (ClassCastException cce) {
      return false;
    }
  }
}
