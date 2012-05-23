package com.apigee.apimodel.common;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIDifference;
import com.apigee.apimodel.APIModelException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class APIUtil
{
  private APIUtil()
  {
  }

  public static void logDifferences(Collection<APIDifference> apiDifferences, Logger logger)
  {
    for (APIDifference difference : apiDifferences)
    {
      logger.info(difference.toString());
    }
  }

  public static List<APIDifference> getAllAPIDifferences(List<API> apis1, List<API> apis2)
    throws APIModelException
  {
    if (apis1.size() != apis2.size()) {
      throw new APIModelException("Different number of APIs in each list");
    }

    List<APIDifference> allAPIDifferences = new ArrayList<APIDifference>();

    for (int i = 0; i < apis1.size(); i++) {
      API a1 = apis1.get(i);
      API a2 = apis2.get(i);
      allAPIDifferences.addAll(a1.getDifferences(a2));
    }

    return allAPIDifferences;
  }

  public static boolean isAPIDifferent(API api1, API api2)
  {
    return !api1.getDifferences(api2).isEmpty();
  }

  public static boolean isAPIsDifferent(List<API> apis1, List<API> apis2)
    throws APIModelException
  {
    return !getAllAPIDifferences(apis1, apis2).isEmpty();
  }
}
