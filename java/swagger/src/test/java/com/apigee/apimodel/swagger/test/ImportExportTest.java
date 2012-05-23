package com.apigee.apimodel.swagger.test;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIModelException;
import com.apigee.apimodel.swagger.SwaggerJSONReader;
import com.apigee.apimodel.swagger.SwaggerJSONWriter;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.apigee.apimodel.common.test.Asserts.*;
import static org.junit.Assert.*;

public class ImportExportTest
{
  public static final String RESOURCES_JSON = "/resources.json";
  public static final String PET_JSON = "/pet.json";
  public static final String USER_JSON = "/user.json";

  private List<API> readModels(String name, InputStream in)
    throws IOException, APIModelException
  {
    List<API> models = SwaggerJSONReader.get().readModel(name, in);

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
    SwaggerJSONWriter.get().writeModel(models, out);
    out.close();

    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

    List<API> models2 = readModels(name, in);

    assertAllAPIsNotDifferent(models, models2);
  }

  @Test
  public void importResourceListing()
    throws IOException, APIModelException
  {
    API api = readApiFromResource("resources", RESOURCES_JSON);

    assertEquals(2, api.getResources().size()); // pet and user
  }

  @Test
  public void importPet()
    throws IOException, APIModelException
  {
    API api = readApiFromResource("pet", PET_JSON);
  }

  @Test
  public void importExportPet()
    throws IOException, APIModelException
  {
    assertRoundtrip("pet", PET_JSON);
  }

  @Test
  public void importExportUser()
    throws IOException, APIModelException
  {
   assertRoundtrip("user", USER_JSON);
  }

}
