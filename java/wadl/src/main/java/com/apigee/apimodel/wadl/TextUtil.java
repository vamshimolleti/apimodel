package com.apigee.apimodel.wadl;

import org.apache.commons.lang3.StringUtils;

public class TextUtil
{

  /**
   * Trim whitespace and newlines from a WADL "doc" string, which comes out of XML looking pretty ugly,
   * and turn it to something reasonable like a single string without all sorts of stuff in it.
   */
  public static String trimWhitespace(String str)
  {
    return StringUtils
      .trim(str)
      .replaceAll("(?m)^\\s+$", "")  // strip lines containing only whitespace
      .replaceAll("([^\\S\n]*\n[^\\S\n]*)+", " "); // replace newlines and whitespace around them with a single space
  }

}
