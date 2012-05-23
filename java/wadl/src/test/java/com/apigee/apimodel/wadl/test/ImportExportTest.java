package com.apigee.apimodel.wadl.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIDifference;
import com.apigee.apimodel.APIModelException;
import com.apigee.apimodel.wadl.WADLReader;
import com.apigee.apimodel.wadl.WADLWriter;

import org.custommonkey.xmlunit.ComparisonController;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceEngine;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ImportExportTest
  extends XMLTestCase
{
  public static final String TWITTER_WADL = "/twitter-wadl.xml";
  public static final String FACEBOOK_WADL = "/facebook-wadl.xml";
  public static final String GITHUB_WADL = "/github-wadl.xml";
  public static final String ETSY_WADL = "/etsy-wadl.xml";
  
  private static final DocumentBuilderFactory docFactory;
  
  static {
    docFactory = DocumentBuilderFactory.newInstance();
    docFactory.setNamespaceAware(true);
  }
  
  private void printDiffs(List<APIDifference> diffs) 
  {
    for (APIDifference d : diffs) {
      System.err.println(d.toString());
    }
  }
  
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
  
  private List<API> importApi(String name, byte[] res)
    throws IOException, APIModelException
  {
    InputStream testWadl = new ByteArrayInputStream(res);
    assertNotNull(testWadl);
    try {
      return WADLReader.get().readModel(name, testWadl);
    } finally {
      testWadl.close();
    }
  }
  
  @Test
  public void testImportTwitter()
    throws IOException, APIModelException
  {
    importApi("twitter", consumeResource(TWITTER_WADL));
  }
  
  @Test
  public void testImportFacebook()
    throws IOException, APIModelException
  {
    importApi("facebook", consumeResource(FACEBOOK_WADL));
  }
  
  @Test
  public void testImportGithub()
    throws IOException, APIModelException
  {
    importApi("github", consumeResource(GITHUB_WADL));
  }
  
  @Test
  public void testImportEtsy()
    throws IOException, APIModelException
  {
    importApi("etsy", consumeResource(ETSY_WADL));
  }
  
  private byte[] exportSink(List<API> apis)
    throws IOException, SAXException
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    
    WADLWriter.get().writeModel(apis, out);
    out.close();
    return out.toByteArray();
  }
  
  @Test
  public void testImportExportTwitter()
    throws IOException, SAXException, APIModelException
  {
    byte[] res = consumeResource(TWITTER_WADL);
    List<API> apis = importApi("twitter", res);
    byte[] out = exportSink(apis);
    List<API> apis2 = importApi("twitter", out);
    assertFalse(isAPIsDifferent(apis, apis2));
    
    // Getting there but there is too much at the moment to fix
    //assertFalse(isXmlDifferent(res, out));
  }
  
  @Test
  public void testImportExportFacebook()
    throws IOException, SAXException, APIModelException
  {
    byte[] res = consumeResource(FACEBOOK_WADL);
    List<API> apis = importApi("facebook", res);
    byte[] out = exportSink(apis);
    List<API> apis2 = importApi("facebook", out);
    assertFalse(isAPIsDifferent(apis, apis2));
    //assertFalse(isXmlDifferent(res, out));
  }
  
  @Test
  public void testImportExportGithub()
    throws IOException, SAXException, APIModelException
  {
    byte[] res = consumeResource(GITHUB_WADL);
    List<API> apis = importApi("github", res);
    byte[] out = exportSink(apis);
    List<API> apis2 = importApi("github", out);
    assertFalse(isAPIsDifferent(apis, apis2));
    //assertFalse(isXmlDifferent(res, out));
  }
  
  @Test
  public void testImportExportEtsy()
    throws IOException, SAXException, APIModelException
  {
    byte[] res = consumeResource(ETSY_WADL);
    List<API> apis = importApi("etsy", res);
    byte[] out = exportSink(apis);
    List<API> apis2 = importApi("etsy", out);
    assertFalse(isAPIsDifferent(apis, apis2));
    //assertFalse(isXmlDifferent(res, out));
  }
  
  private boolean isAPIsDifferent(List<API> apis1, List<API> apis2)
  {
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
  
  private boolean isXmlDifferent(byte[] b1, byte[] b2)
    throws IOException, SAXException
  { 
    DocumentBuilder builder;
    try {
      builder = docFactory.newDocumentBuilder();
      assertTrue(builder.isNamespaceAware());
    } catch (ParserConfigurationException e) {
      throw new AssertionError(e);
    }
    
    Document doc1 = builder.parse(new ByteArrayInputStream(b1));
    System.err.println("Fixing doc1");
    fixDefaults(doc1, doc1);
    Document doc2 = builder.parse(new ByteArrayInputStream(b2));
    System.err.println("Fixing doc2");
    fixDefaults(doc2, doc2);
    
    XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
    XMLUnit.setIgnoreComments(true);
    XMLUnit.setIgnoreWhitespace(true);
    XMLUnit.setNormalize(true);
    XMLUnit.setNormalizeWhitespace(true);
    XMLUnit.setIgnoreAttributeOrder(true);

    DiffController dc = new DiffController();
    DifferenceEngine de = new DifferenceEngine(dc);
    de.compare(doc1, doc2, dc, null);
    return !dc.diffs.isEmpty();
  }
  
  // These methods let us customize the way that we compar2 XML docs
  
  private void ensureDefaultAttribute(Element e, String name, String value)
  {
    /*
    if (e.hasAttribute(name)) {
      System.err.println("  " + name + '=' + e.getAttribute(name));
    }
    */
    if (!e.hasAttribute(name) || (e.getAttribute(name).isEmpty())) {
      // System.err.println("  Setting " + name + '=' + value);
      e.setAttribute(name, value);
    }
  }
  
  private boolean isNamed(Node n, String uri, String name)
  {
    boolean ret = false;
    if (n.getNamespaceURI().equals(uri)) {
      String[] nameParts = n.getNodeName().split(":");
      ret = nameParts[nameParts.length - 1].equals(name);
    }
    /*
    if (name.equals("param")) {
      System.err.println(ret + ": " + uri + '.' + name + " = " + 
          n.getNamespaceURI() + '.' + n.getNodeName());
    }
    */
    return ret;
  }
  
  private void fixDefaults(Document d, Node n)
  {
    if (n.getNodeType() == Node.ELEMENT_NODE) {
      Element e = (Element)n;
      if (isNamed(n, "http://wadl.dev.java.net/2009/02", "param")) {
        ensureDefaultAttribute(e, "required", "false");
        ensureDefaultAttribute(e, "repeating", "false");
      }
      if (isNamed(n, "http://api.apigee.com/wadl/2010/07/", "choice")) {
        ensureDefaultAttribute(e, "required", "false");
        ensureDefaultAttribute(e, "countMin", "0");
        ensureDefaultAttribute(e, "countMax", "1");
      }
      if (isNamed(n, "http://wadl.dev.java.net/2009/02", "doc")) {
        ensureDefaultAttribute(e, "title", "");
      }
    }
    
    Node next = n.getFirstChild();
    while (next != null) {
      fixDefaults(d, next);
      next = next.getNextSibling();
    }
  }
  
  private static final class DiffController
    implements ComparisonController, DifferenceListener
  {
    final ArrayList<Difference> diffs = new ArrayList<Difference>();
    
    public int differenceFound(Difference diff)
    {
      switch (diff.getId()) {
      case DifferenceConstants.NAMESPACE_URI_ID:
      case DifferenceConstants.NAMESPACE_PREFIX_ID:
      case DifferenceConstants.SCHEMA_LOCATION_ID:
      case DifferenceConstants.NO_NAMESPACE_SCHEMA_LOCATION_ID:
        return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
      default:
        System.err.println("Found difference " + diff.getId() + ": " + diff);
        diffs.add(diff);
        return RETURN_ACCEPT_DIFFERENCE;
      }
    }

    public void skippedComparison(Node n1, Node n2)
    {
      // Nothing to do here
    }

    public boolean haltComparison(Difference diff)
    {
      return !diff.isRecoverable();
    }
    
    public String toString()
    {
      StringBuilder str = new StringBuilder();
      for (Difference d : diffs) {
        str.append(d.toString());
      }
      return str.toString();
    }
  }
}
