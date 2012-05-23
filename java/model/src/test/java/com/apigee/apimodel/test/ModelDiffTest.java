package com.apigee.apimodel.test;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.apigee.apimodel.APIDifference;
import com.apigee.apimodel.impl.APIImpl;

public class ModelDiffTest
  extends TestCase
{
  private void printDiffs(List<APIDifference> diffs) 
  {
    for (APIDifference d : diffs) {
      System.err.println(d.toString());
    }
  }
  
  @Test
  public void testDiff1()
  {
    APIImpl api1 = new APIImpl();
    api1.setBasePath("/foo");
    api1.setName("Foo");
    
    APIImpl api2 = new APIImpl();
    api2.setBasePath("/bar");
    api2.setName("Bar");
    
    APIImpl api3 = new APIImpl();
    api3.setBasePath("/bar");
    api3.setName("Bar");
    
    System.err.println("Diffs between api2 and api3:");
    printDiffs(api2.getDifferences(api3));
    
    System.err.println("Diffs between api1 and api2:");
    printDiffs(api1.getDifferences(api3));
    
    assertTrue(api2.equals(api3));
    assertFalse(api1.equals(api2));
  }
  
  
}
