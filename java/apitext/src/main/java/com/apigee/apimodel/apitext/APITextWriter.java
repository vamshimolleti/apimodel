package com.apigee.apimodel.apitext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIWriter;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class APITextWriter
  implements APIWriter
{
  public static final String MAIN_TEMPLATE = "apitext-template.md";
  
  private static final APITextWriter writer = new APITextWriter();
  
  private APITextWriter()
  {
  }
  
  public static final APITextWriter get() {
    return writer;
  }
  
  public void writeModel(List<API> m, OutputStream out, Logger log)
      throws IOException
  {
    Configuration fmConfig = new Configuration();
    fmConfig.setClassForTemplateLoading(APITextWriter.class, "/");
    fmConfig.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
    Template fmTemplate = fmConfig.getTemplate(MAIN_TEMPLATE);
    
    HashMap<String, Object> top = new HashMap<String, Object>();
    ArrayList<Object> models = new ArrayList<Object>();
    top.put("models", m);
    
    OutputStreamWriter writer = new OutputStreamWriter(out);
    try {
      fmTemplate.process(top, writer);
    } catch (TemplateException te) {
      throw new IOException(te);
    }
  }
  
  public void writeModel(List<API> model, OutputStream out)
      throws IOException
  {
    writeModel(model, out, Logger.getLogger(DEFAULT_WRITER_LOGGER));
  }
}
