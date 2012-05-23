package com.apigee.apimodel;

public class APIDifference
{
  private String path;
  private Object o1;
  private Object o2;
  private String description;
  
  public APIDifference()
  {
  }
  
  public APIDifference(String path, Object o1, Object o2, String description)
  {
    this.path = path;
    this.o1 = o1;
    this.o2 = o2;
    this.description = description;
  }
  
  public String getPath()
  {
    return path;
  }
  public void setPath(String path)
  {
    this.path = path;
  }
  public Object getObject1()
  {
    return o1;
  }
  public void setObject1(Object o1)
  {
    this.o1 = o1;
  }
  public Object getObject2()
  {
    return o2;
  }
  public void setObject2(Object o2)
  {
    this.o2 = o2;
  }
  public String getDescription()
  {
    return description;
  }
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public String toString()
  {
    return path + ':' + description;
  }
}
