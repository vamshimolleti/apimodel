package com.apigee.apimodel.jpa.test;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIModelException;
import com.apigee.apimodel.impl.APIImpl;
import com.apigee.apimodel.wadl.WADLReader;

public class JPABasicTest
{
  private static EntityManagerFactory factory;
  private EntityManager em;
  
  @BeforeClass
  public static void initJPA()
  {
    factory = Persistence.createEntityManagerFactory("JPATest");
  }
  
  @Before
  public void getEM()
  {
    em = factory.createEntityManager();
  }
  
  @After
  public void closeEM()
  {
    em.close();
  }
  
  @Test
  public void testStoreBasicModel()
  {
    em.getTransaction().begin();
    APIImpl api = new APIImpl();
    api.setName("TestAPI");
    api.setBasePath("http://localhost:10001");
    em.persist(api);
    em.getTransaction().commit();
  }
  
  @Test
  public void testStoreLoadBasicModel()
  {
    em.getTransaction().begin();
    APIImpl api = new APIImpl();
    api.setName("TestAPI2");
    api.setBasePath("http://localhost:10001");
    em.persist(api);
    em.getTransaction().commit();
    
    em.getTransaction().begin();
    Query q = em.createQuery("select x from APIImpl x where x.name = :name");
    q.setParameter("name", "TestAPI2");
    List<API> results = q.getResultList();
    assertFalse(results.isEmpty());
    assertEquals(api, results.get(0));
    em.getTransaction().commit();
  }
  
  @Test
  public void testStoreFromWADL()
    throws IOException, APIModelException
  {
    List<API> apis = 
      WADLReader.get().readModel("twitter", getClass().getResourceAsStream("/twitter-wadl.xml"));
    assertFalse(apis.isEmpty());
    
    em.getTransaction().begin();
    em.persist(apis.get(0));
    em.getTransaction().commit();
  }
  
  @Test
  public void testStoreLoadFromWADL()
    throws IOException, APIModelException
  {
    List<API> apis = 
        WADLReader.get().readModel("twitter2", getClass().getResourceAsStream("/twitter-wadl.xml"));
    assertFalse(apis.isEmpty());

    em.getTransaction().begin();
    em.persist(apis.get(0));
    em.getTransaction().commit();
    
    em.getTransaction().begin();
    Query q = em.createQuery("select x from APIImpl x where x.name = :name");
    q.setParameter("name", "twitter2");
    List<API> results = q.getResultList();
    assertFalse(results.isEmpty());
    assertEquals(apis.get(0), results.get(0));
    em.getTransaction().commit();
  }
}
