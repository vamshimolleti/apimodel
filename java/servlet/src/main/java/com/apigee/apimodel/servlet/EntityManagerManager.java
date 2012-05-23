package com.apigee.apimodel.servlet;

import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerManager
{
  private static final EntityManagerManager em = new EntityManagerManager();
  
  private final ConcurrentHashMap<String, EntityManagerFactory> factories =
    new ConcurrentHashMap<String, EntityManagerFactory>();
  
  private EntityManagerManager() 
  {
  }
  
  public static EntityManagerManager get() {
    return em;
  }
  
  public EntityManagerFactory getFactory(String name)
  {
    if (!factories.containsKey(name)) {
      EntityManagerFactory f = Persistence.createEntityManagerFactory(name);
      factories.putIfAbsent(name, f);
    }
    return factories.get(name);
  }
}
