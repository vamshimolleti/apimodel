package com.apigee.apimodel.servlet.testservlets;

import com.apigee.apimodel.servlet.APIResourceHandler;
import com.apigee.apimodel.servlet.test.TestObject;

public class TestResource 
  extends APIResourceHandler
{
  public Class<?> getRequestClass() {
    return TestObject.class;
  }
  
  public Object get()
  {
    TestObject o = new TestObject();
    o.setName("Hello");
    o.setValue(123);
    return o;
  }
  
  public Object post(Object req)
  {
    TestObject o = (TestObject)req;
    return o;
  }
}
