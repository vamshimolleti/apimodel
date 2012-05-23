package com.apigee.apimodel.wadl.test;

import com.apigee.apimodel.wadl.TextUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrimWhitespaceTest
{
  @Test
  public void trim()
  {
    String dirty = "\n\t\t\t\t\t  Returns the users using their username.  \n\t\t\t\t  ";
    String clean = "Returns the users using their username.";

    assertEquals(clean, TextUtil.trimWhitespace(dirty));
  }

  @Test
  public void linebreaks()
  {
    String dirty = "\n\t\t\t\t\t  Returns \n        the users\n    \t    using their \t\n  \t username.  \n\t\t\t\t  ";
    String clean = "Returns the users using their username.";

    assertEquals(clean, TextUtil.trimWhitespace(dirty));
  }


  @Test
  public void multilineSimple()
  {
    String dirty = "x\n     \t   \t\ny";
    String clean = "x y";

    assertEquals(clean, TextUtil.trimWhitespace(dirty));
  }

  @Test
  public void multilineComplex()
  {
    String dirty = "\n\t  Line\none of two.    \n    \n\n\n\t   \n   \tLine two of two.  \n\t   ";
    String clean = "Line one of two. Line two of two.";

    assertEquals(clean, TextUtil.trimWhitespace(dirty));
  }

}
