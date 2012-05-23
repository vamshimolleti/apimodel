package com.apigee.apimodel.servlet;

import java.io.FilterOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * We use this class when passing OutputStream instances to resource handler objects.
 * We do this because we want to set their output stream before calling their
 * method to create the output, but at the same time we can't start to
 * send data until all the headers have been set.
 */

public class LazyServletOutputStream 
  extends FilterOutputStream
{
  private final HttpServletResponse resp;
  private final ResourceClassInfo info;
  private final Object target;
  private boolean initialized;
  private boolean closed;
  
  public LazyServletOutputStream(HttpServletResponse resp, ResourceClassInfo info, Object target)
  {
    super(null);
    this.resp = resp;
    this.info = info;
    this.target = target;
  }

  public void write(int b) 
  throws IOException
  {
    initialize();
    super.write(b);
  }

  public void write(byte[] b)
  throws IOException
  {
    initialize();
    super.write(b);
  }

  public void write(byte[] b, int o, int l)
  throws IOException
  {
    initialize();
    super.write(b, o, l);
  }

  public void close()
  throws IOException
  {
    if (!closed) {
      if (initialized) {
        super.close();
      } else {
        info.updateResponse(resp,target);
      }
      closed = true;
    }
  }

  public void flush()
  throws IOException
  {
    if (initialized) {
      super.flush();
    }
  }
  
  private void initialize()
    throws IOException
  {
    if (!initialized) {
      info.updateResponse(resp, target);
      out = resp.getOutputStream();
      initialized = true;
    }
  }
}
