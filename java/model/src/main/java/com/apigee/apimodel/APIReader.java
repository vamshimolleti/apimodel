package com.apigee.apimodel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

public interface APIReader
{
  static final String DEFAULT_READER_LOGGER = "default";
  
  List<API> readModel(String name, InputStream in)
    throws APIModelException, IOException;
  
  List<API> readModel(String name, InputStream in, Logger log)
    throws APIModelException, IOException; 
}
