package com.apigee.apimodel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

public interface APIWriter
{
  static final String DEFAULT_WRITER_LOGGER = "default";
  
  void writeModel(List<API> model, OutputStream out)
    throws IOException;
  
  void writeModel(List<API> model, OutputStream out, Logger log)
      throws IOException;
}
