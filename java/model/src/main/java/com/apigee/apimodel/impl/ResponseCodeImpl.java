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

import com.apigee.apimodel.ResponseCode;

@Entity
@Table(name="responsecodes")
public class ResponseCodeImpl 
  extends AbstractDocumentedImpl
  implements ResponseCode
{
  @Id @GeneratedValue
  private long id;
  @Basic
  private int code;
  @Basic(optional=true) @Column(length=512)
  private String message;
  @Basic
  private int success;
  
  @Basic(optional=true) @ManyToOne
  private OperationImpl parentOperation;
  @Basic(optional=true) @ManyToOne
  private ResourceImpl parentResource;
  
  public long getId() {
    return id;
  }
  
  public int getCode()
  {
    return code;
  }

  public void setCode(int code)
  {
    this.code = code;
  }

  public boolean isSuccess()
  {
    return (success != 0);
  }

  public void setSuccess(boolean success)
  {
    this.success = success ? 1 : 0;
  }

  @JsonSerialize(include=Inclusion.NON_EMPTY)
  public String getMessage()
  {
    return message;
  }

  public void setMessage(String message)
  {
    this.message = message;
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
      ResponseCodeImpl r = (ResponseCodeImpl)o;
      if ((id != r.id) || (code != r.code) ||
          (success != r.success) ||
          !Utils.same(message, r.message)) {
        return false;
      }
      return super.equals(o);
    } catch (ClassCastException cce) {
      return false;
    }
  }
}
