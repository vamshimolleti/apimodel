package com.apigee.apimodel.apitext.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIDifference;
import com.apigee.apimodel.APIModelException;
import com.apigee.apimodel.Resource;
import com.apigee.apimodel.apitext.APITextReader;
import com.apigee.apimodel.apitext.APITextWriter;
import com.apigee.apimodel.impl.ParameterImpl;

public class ImportExportTest
  extends TestCase
{
  private byte[] consumeResource(String res)
      throws IOException
  {
    InputStream testWadl = getClass().getResourceAsStream(res);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] tmp = new byte[8192];
    int bytesRead;
    do {
      bytesRead = testWadl.read(tmp);
      if (bytesRead > 0) {
        out.write(tmp, 0, bytesRead);
      }
    } while (bytesRead > 0);
    testWadl.close();
    out.close();
    return out.toByteArray();
  }
  
  private List<API> readModel(byte[] res)
    throws IOException, APIModelException
  {
    ByteArrayInputStream in = new ByteArrayInputStream(res);
    try {
      List<API> models = APITextReader.get().readModel("twitter", in);
      assertFalse(models.isEmpty());
      return models;
    } finally {
      in.close();
    }
  }
  
  @Test
  public void testImport()
    throws IOException, APIModelException
  {
    byte[] res = consumeResource("/twitter.md");
    readModel(res);
  }
  
  @Test
  public void testImportExportInternal()
    throws IOException, APIModelException
  {
    byte[] res = consumeResource("/twitter.md");
    List<API> models = readModel(res);
    
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    APITextWriter.get().writeModel(models, out);
    out.close();
    byte[] nextRes = out.toByteArray();
    List<API> models2 = readModel(nextRes);

    assertFalse(isAPIsDifferent(models, models2));
  }
  
  @Test
  public void testImportExportDiffWorks()
    throws IOException, APIModelException
  {
    byte[] res = consumeResource("/twitter.md");
    List<API> models = readModel(res);
    
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    APITextWriter.get().writeModel(models, out);
    out.close();
    byte[] nextRes = out.toByteArray();
    List<API> models2 = readModel(nextRes);
    
    API a1 = models2.get(0);
    Resource r1 = a1.getResources().iterator().next();
    r1.setPath("/bad");
    ParameterImpl p = new ParameterImpl();
    p.setName("badbadbad");
    r1.addQueryParameter(p);

    assertTrue(isAPIsDifferent(models, models2));
  }
  
  private void printDiffs(List<APIDifference> diffs) 
  {
    for (APIDifference d : diffs) {
      System.err.println(d.toString());
    }
  }
  
  private boolean isAPIsDifferent(List<API> apis1, List<API> apis2)
  {
    if (apis1.size() != apis2.size()) {
      System.err.println("Different number of APIs in each list");
      return false;
    }
    for (int i = 0; i < apis1.size(); i++) {
      API a1 = apis1.get(i);
      API a2 = apis2.get(i);
      List<APIDifference> diffs = a1.getDifferences(a2);
      printDiffs(diffs);
      if (!diffs.isEmpty()) {
        return true;
      }
    }
    return false;
  }
}
