package com.apigee.apimodel.processor.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.apigee.apimodel.Resource;

public class PathEntry
{
  private static final Logger log = Logger.getLogger("apimodel");
  
  private final Pattern re;
  private final Resource resource;
  private List<String> params = Collections.EMPTY_LIST;
  private Object context;
  
  public PathEntry(Resource res)
  {
    StringBuilder reb = new StringBuilder();
    StringBuilder paramName = new StringBuilder();
    boolean inParam = false;
    
    this.resource = res;
    
    reb.append('^');
    for (int i = 0; i < res.getPath().length(); i++) {
      char c = res.getPath().charAt(i);
      if (inParam && (c != '}')) {
        paramName.append(c);
      } else {
        switch (c) 
        {
        case '{':
          inParam = true;
          paramName = new StringBuilder();
          break;
        case '}':
          reb.append("([^/]+)");
          if (params == Collections.EMPTY_LIST) {
            params = new ArrayList<String>();
          }
          params.add(paramName.toString());
          inParam = false;
          break;
        case '\\':
        case '.':
        case '^':
        case '$':
        case '?':
        case '*':
        case '+':
        case '[':
        case ']':
        case '(':
        case ')':
          reb.append('\\');
          reb.append(c);
          break;
        default:
          reb.append(c);
        }
      }
    }
    reb.append('$');
    
    re = Pattern.compile(reb.toString());
  }
  
  public boolean match(String path, Map<String, String> templateParams)
  {
    Matcher m = re.matcher(path);
    
    /*
    if (log.isLoggable(Level.FINEST)) {
      log.finest('\"' + path + "\" : \"" + re.pattern() + "\" = " + m.matches());
    }
    */
    if (m.matches()) {
      int g = 1;
      for (String n : params) {
        templateParams.put(n, m.group(g++));
      }
      return true;
    }
    return false;
  }
  
  public Resource getResource() {
    return resource;
  }
  
  public Object getContext() {
    return context;
  }
  
  public void setContext(Object context) {
    this.context = context;
  }
}
