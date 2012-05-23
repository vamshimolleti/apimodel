package com.apigee.apimodel.console.test;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIModelException;
import com.apigee.apimodel.console.ConsoleWriter;
import com.apigee.apimodel.wadl.WADLReader;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class ExportTest
{
  public static final String TWITTER_WADL = "/twitter-wadl.xml";
  public static final String FACEBOOK_WADL = "/facebook-wadl.xml";
  public static final String GITHUB_WADL = "/github-wadl.xml";

  public static final String TWITTER_JSON = "/twitter-wadl.json";
  public static final String FACEBOOK_JSON = "/facebook-wadl.json";
  public static final String GITHUB_JSON = "/github-wadl.json";

  // read in WADLs and write them out as Console JSON
  private List<API> readModels(String name, InputStream in)
    throws IOException, APIModelException
  {
    List<API> models = WADLReader.get().readModel(name, in);

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

  @Test
  public void exportGithub()
    throws IOException, APIModelException
  {
    List<API> models = readModelsFromResource("github", GITHUB_WADL);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    // FileOutputStream out = new FileOutputStream("github.out.json");
    ConsoleWriter.get().writeModel(models, out);
    out.close();

    System.err.print(new String(out.toByteArray()));
  }

}
