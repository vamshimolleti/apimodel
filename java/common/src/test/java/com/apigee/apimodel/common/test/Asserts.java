package com.apigee.apimodel.common.test;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIDifference;
import com.apigee.apimodel.APIModelException;
import com.apigee.apimodel.common.APIUtil;

import java.util.List;
import java.util.logging.Logger;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class Asserts
{
  public static final Logger logger = Logger.getLogger(APIModelTestCase.DEFAULT_TEST_LOGGER);

  private Asserts()
  {
  }

  public static void assertAPIsNotDifferent(API api1, API api2)
  {
    assertAPIDifferencesEmpty(api1.getDifferences(api2));
  }

  public static void assertAllAPIsNotDifferent(List<API> apis1, List<API> apis2)
  {
    List<APIDifference> apiDifferences = null;
    try {
      apiDifferences = APIUtil.getAllAPIDifferences(apis1, apis2);
    } catch (APIModelException e) {
      fail(e.getMessage());
    }

    assertAPIDifferencesEmpty(apiDifferences);
  }

  private static void assertAPIDifferencesEmpty(List<APIDifference> apiDifferences)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("APIs are different\n");

    for (APIDifference apiDifference : apiDifferences) {
      sb.append(apiDifference.toString());
      sb.append('\n');
    }

    assertTrue(sb.toString(), apiDifferences.isEmpty());
  }

}
