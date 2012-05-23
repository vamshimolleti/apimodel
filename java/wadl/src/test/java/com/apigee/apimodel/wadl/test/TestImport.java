package com.apigee.apimodel.wadl.test;

import java.io.FileInputStream;
import java.util.List;

import com.apigee.apimodel.API;
import com.apigee.apimodel.wadl.WADLReader;
import com.apigee.apimodel.wadl.WADLWriter;

public class TestImport
{
  public static void main(String[] args)
  {
    if (args.length != 2) {
      System.err.println("Usage: TestImport <name> <wadl file>");
      return;
    }
    
    try {
      FileInputStream in = new FileInputStream(args[0]);
      try {
        List<API> models = WADLReader.get().readModel(args[1], in);
        WADLWriter.get().writeModel(models, System.out);
      } finally {
        in.close();
      }
      
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }
}
