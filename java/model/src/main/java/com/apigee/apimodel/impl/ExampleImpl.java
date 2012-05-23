package com.apigee.apimodel.impl;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.apigee.apimodel.Example;

@Entity
@Table(name="examples")
public class ExampleImpl 
  extends AbstractDocumentedImpl 
  implements Example
{
  @Id @GeneratedValue
  private long id;
  @Column(length=128)
  private String mediaType;
  @Basic(optional=true) @Column(length=8192)
  private String contents;
  @Basic(optional=true) @Column(length=512)
  private String uri;
  
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
  public String getContents()
  {
    return contents;
  }

  public void setContents(String contents)
  {
    this.contents = contents;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getUri()
  {
    return uri;
  }

  public void setUri(String uri)
  {
    this.uri = uri;
  }
  
  public boolean equals(Object o)
  {
    try {
      ExampleImpl ex = (ExampleImpl)o;
      if ((id != ex.id) || !Utils.same(mediaType, ex.mediaType) ||
          !Utils.same(contents, ex.contents) ||
          !Utils.same(uri, ex.uri)) {
        return false;
      }
      return super.equals(o);
    } catch (ClassCastException cce) {
      return false;
    }
  }
}
