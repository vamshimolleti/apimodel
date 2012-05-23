package com.apigee.apimodel.jpa;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBUpdater
{
  public static void main(String[] args)
  {
    if (args.length != 3) {
      System.err.println("Usage: DBUpdater <model name> <action> <properties file>");
      return;
    }
    
    String model = args[0];
    String action = args[1];
    
    Properties apiProps = new Properties();
    FileInputStream in;
    try {
      in = new FileInputStream(args[2]);
      try {
        apiProps.load(in);
      } finally {
        in.close();
      }
    } catch (IOException ioe) {
      System.err.println(ioe);
      return;
    }
    
    HashMap<String, Object> props = new HashMap<String, Object>();
    for (Map.Entry<Object, Object> e : apiProps.entrySet()) {
      props.put((String)e.getKey(), e.getValue());
    }
    props.put("hibernate.show_sql", "true");
    
    if (action.equals("validate")) {
      props.put("hibernate.hbm2ddl.auto", "validate");
    } else if (action.equals("create")) {
      props.put("hibernate.hbm2ddl.auto", "create");
    } else if (action.equals("create-drop")) {
      props.put("hibernate.hbm2ddl.auto", "create-drop");
    } else if (action.equals("update")) {
      props.put("hibernate.hbm2ddl.auto", "update");
    } else {
      System.err.println("Invalid action");
      return;
    }
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(model, props);
    EntityManager em = emf.createEntityManager();
  }
}
