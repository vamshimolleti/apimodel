package com.apigee.apimodel.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.apigee.apimodel.Documented;
import com.apigee.apimodel.Parameter;

@MappedSuperclass
public abstract class AbstractDocumentedImpl
  implements Documented
{
  @Basic(optional=true) @Column(length=256)
  protected String nodeId;
  
  @Basic(optional=true) @Lob
  protected String documentation;
  
  @Basic(optional=true) @Column(length=256)
  protected String documentationURI;
  
  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getDocumentation()
  {
    return documentation;
  }

  public void setDocumentation(String doc)
  {
    this.documentation = doc;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getDocumentationURI()
  {
    return documentationURI;
  }

  public void setDocumentationURI(String uri)
  {
    this.documentationURI = uri;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getNodeId()
  {
    return nodeId;
  }

  public void setNodeId(String nodeId)
  {
    this.nodeId = nodeId;
  }
  
  public boolean equals(Object o)
  {
    return true;
    /*
     * We may wish to compare docs optionally because they imply the same MODEL
    try {
      AbstractDocumentedImpl ad = (AbstractDocumentedImpl)o;
      return Utils.same(nodeId, ad.nodeId) && Utils.same(documentation, ad.documentation) &&
             Utils.same(documentationURI, ad.documentationURI);
    } catch (ClassCastException cce) {
      return false;
    }
    */
  }
}
