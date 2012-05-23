package com.apigee.apimodel.iodocs.test;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIModelException;
import com.apigee.apimodel.iodocs.IODocsReader;
import com.apigee.apimodel.iodocs.IODocsWriter;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.apigee.apimodel.common.test.Asserts.assertAllAPIsNotDifferent;
import static org.junit.Assert.*;

public class ImportExportTest
{
  public static final String APICONFIG_JSON = "/apiconfig.json";
  public static final String FANFEEDR_JSON = "/fanfeedr.json";

  private List<API> readModels(String name, InputStream in)
    throws IOException, APIModelException
  {
    List<API> models = IODocsReader.get().readModel(name, in);

    assertNotNull(models);
    assertFalse("No models returned", models.isEmpty());

    return models;
  }

  private List<API> readModelsFromResource(String name, String res)
    throws IOException, APIModelException
  {
    return readModels(name, getClass().getResourceAsStream(res));
  }

  private API readApiFromResource(String name, String res)
    throws IOException, APIModelException
  {
    List<API> models = readModelsFromResource(name, res);

    assertEquals("Resource contains more than one API", 1, models.size());

    API api = models.get(0);
    assertNotNull(api);

    return api;
  }

  private void assertRoundtrip(String name, String res)
    throws IOException, APIModelException
  {
    List<API> models = readModelsFromResource(name, res);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    IODocsWriter.get().writeModel(models, out);
    out.close();

    System.err.print(new String(out.toByteArray()));

    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

    List<API> models2 = readModels(name, in);

    assertAllAPIsNotDifferent(models, models2);
  }

  @Test
  public void importExportFanfeedr()
    throws IOException, APIModelException
  {
    assertRoundtrip("fanfeedr", FANFEEDR_JSON);
  }

}
