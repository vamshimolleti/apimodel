package com.apigee.apimodel.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.apigee.apimodel.API;
import com.apigee.apimodel.APIModelException;
import com.apigee.apimodel.apitext.APITextReader;
import com.apigee.apimodel.apitext.APITextWriter;
import com.apigee.apimodel.wadl.WADLReader;
import com.apigee.apimodel.wadl.WADLWriter;

public class Convert
{
  private static void printUsage()
  {
    System.err.println("Usage: Convert <input file> [output file]");
  }
  
  private static String getExtension(String fn)
  {
    int i = fn.lastIndexOf('.');
    if ((i < 0) || (fn.length() <= (i + 1))) {
      return "";
    }
    return fn.substring(i + 1);
  }
  
  public static void main(String[] args)
  {
    if ((args.length < 1) || (args.length > 2)) {
      printUsage();
      return;
    } 
    
    String inFile = args[0];
    String outFile = null;
    
    if (args.length == 2) {
      outFile = args[1];
    }
    
    FileInputStream inStream;
    try {
      inStream = new FileInputStream(inFile);
    } catch (IOException ioe) {
      System.err.println("Cannot open input file \"" + inFile + "\": " + ioe);
      return;
    }
    
    List<API> models = null;
    
    try {
      String ext = getExtension(inFile);
      if (ext.equals("wadl") || ext.equals("xml")) {
        models = WADLReader.get().readModel("tmp", inStream);
      } else if (ext.equals("md") || ext.equals("txt")) {
        models = APITextReader.get().readModel("tmp", inStream);
      } else {
        System.err.println("The input file extension " + ext + " is not recognized.");
        printUsage();
        return;
      }
    } catch (APIModelException ae) {
      System.err.println("Error reading API model: " + ae);
      return;
    } catch (IOException ie) {
      System.err.println("Error reading API model file: " + ie);
      return;
    } finally {
      try {
        inStream.close();
      } catch (IOException ioe) {
        // This is a silly exception
      }
    }
    
    assert(models != null);
    
    OutputStream outStream;
    String ext;
    
    if (outFile == null) {
      outStream = System.out;
      ext = "md";
    
    } else {
      try {
        outStream = new FileOutputStream(outFile);
      } catch (IOException ioe) {
        System.err.println("Cannot open output file \"" + outFile + "\": " + ioe);
        return;
      }
      ext = getExtension(outFile);
    }
    
    try {
      if (ext.equals("wadl") || ext.equals("xml")) {
        WADLWriter.get().writeModel(models, outStream);
      } else if (ext.equals("md") || ext.equals("txt")) {
        APITextWriter.get().writeModel(models, outStream);
      } else {
        System.err.println("The output file extension " + ext + " is not recognized.");
        printUsage();
        return;
      }
    } catch (IOException ioe) {
      System.err.println("Error writing API model: " + ioe);
      return;
    } finally {
      try {
        if (outStream != System.out) {
          outStream.close();
        }
      } catch (IOException ioe) {
        // Useless
      }
    }
  }  
}

