package controllers;

import play.mvc.Controller;

public class Default
  extends Controller
{
  public static void notFound()
  {
    error(404, "Not found");
  }
}
