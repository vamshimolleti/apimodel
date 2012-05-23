package com.apigee.apimodel.jpa;

import javax.persistence.EntityManager;

import com.apigee.apimodel.API;
import com.apigee.apimodel.impl.APIImpl;

public class JPAWriter 
{
  private static JPAWriter myself = new JPAWriter();
  
  public static JPAWriter get() {
    return myself;
  }
  
  private JPAWriter()
  {
  }
  
  public void write(APIImpl a, EntityManager mgr)
  {
    mgr.persist(a);
  }
}
