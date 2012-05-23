package com.apigee.apimodel.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.apigee.apimodel.APIDifference;
import com.apigee.apimodel.Parameter;
import com.apigee.apimodel.Parameter.Type;

public class Utils
{
  public static boolean same(Object s1, Object s2) 
  {
    if (s1 == null) {
      return s2 == null;
    }
    return s1.equals(s2);
  }
  
  public static void compareMaps(String name, String path,
                                 Map<?, ?> m1, Map<?, ?> m2, 
                                 List<APIDifference> diffs)
  {
    HashSet<Object> missing = new HashSet<Object>(m2.keySet());
    for (Map.Entry<?, ?> e1 : m1.entrySet()) {
      if (m2.containsKey(e1.getKey())) {
        if (!e1.getValue().equals(m2.get(e1.getKey()))) {
          diffs.add(new APIDifference(path, m1, m2, 
                    name + ' ' + e1.getKey() + " differs between APIs"));
        }
      } else {
        diffs.add(new APIDifference(path, m1, m2, 
                  name + ' ' + e1.getKey() + " part of first API but not second"));
      }
      missing.remove(e1.getKey());
    }
    for (Object m : missing) {
      diffs.add(new APIDifference(path, m1, m2, 
                name + ' ' + m + " part of second API but not first"));
    }
  }
  
  public static <T, U> void mergeInto(Map<T, U> m1, Map<T, U> m2)
  {
    for (Map.Entry<T, U> e : m2.entrySet()) {
      if (!m1.containsKey(e.getKey())) {
        m1.put(e.getKey(), e.getValue());
      }
    }
  }
  
  public static void mergeParams(List<Parameter> c1, List<Parameter> c2)
  {
    HashMap<PKey, Parameter> p1Map = new HashMap<PKey, Parameter>();
    for (Parameter p : c1) {
      p1Map.put(new PKey(p), p);
    }
    for (Parameter p : c2) {
      PKey pk = new PKey(p);
      if (!p1Map.containsKey(pk)) {
        p1Map.put(pk, p);
      }
    }
    c1.clear();
    c1.addAll(p1Map.values());
  }
  
  /**
   * Merge matrix and template parameters from the parent, per the WADL spec.
   * However, do not merge other parameters from the parent API -- we will add
   * these later.
   */
  public static void mergeNestedParams(List<Parameter> c1, List<Parameter> c2)
  {
    HashMap<PKey, Parameter> p1Map = new HashMap<PKey, Parameter>();
    for (Parameter p : c1) {
      p1Map.put(new PKey(p), p);
    }
    for (Parameter p : c2) {
      if ((p.getType() == Type.TEMPLATE) ||
          (p.getType() == Type.MATRIX)) {
        PKey pk = new PKey(p);
        if (!p1Map.containsKey(pk)) {
          p1Map.put(pk, p);
        }
      }
    }
    c1.clear();
    c1.addAll(p1Map.values());
  }
  
  /* Utility functions for subclasses */
  
  public static int countParameterTypes(List<Parameter> params, Parameter.Type type)
  {
    int count = 0;
    for (Parameter p : params) {
      if (p.getType() == type) {
        count++;
      }
    }
    return count;
  }
  
  public static List<Parameter> getParamCollection(List<Parameter> params, Parameter.Type type)
  {
    ArrayList<Parameter> ret = new ArrayList<Parameter>(params.size());
    for (Parameter p : params) {
      if (p.getType() == type) {
        ret.add(p);
      }
    }
    return ret;
  }
  
  public static Map<String, Parameter> getParamMap(List<Parameter> params, Parameter.Type type)
  {
    HashMap<String, Parameter> ret = new HashMap<String, Parameter>(params.size());
    for (Parameter p : params) {
      if (p.getType() == type) {
        ret.put(p.getName(), p);
      }
    }
    return ret;
  }
  
  private static final class PKey
  {
    String name;
    Parameter.Type type;
    
    PKey(Parameter p)
    {
      this.name = p.getName();
      this.type = p.getType();
    }
    
    public boolean equals(Object o)
    {
      try {
        PKey p = (PKey)o;
        return ((type == p.type) && same(name, p.name));
      } catch (ClassCastException cce) {
        return false;
      }
    }
  }
}
