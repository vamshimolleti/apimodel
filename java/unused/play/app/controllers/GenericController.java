package controllers;

import java.io.IOException;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.ObjectMapper;

import play.mvc.Controller;

public abstract class GenericController 
  extends Controller
{
  private static final ObjectMapper mapper = new ObjectMapper();
  
  private static final Pattern xmlPattern =
      Pattern.compile("^(text|application)/([^/+]+\\+)?xml(;.*)?");
  private static final Pattern jsonPattern =
      Pattern.compile("^(text|application)/([^/+]+\\+)?json(;.*)?");
  private static final Pattern textPattern =
      Pattern.compile("^text/plain(;.*)?");
  private static final Pattern formPattern =
      Pattern.compile("^application/x-www-form-urlencoded(;.*)?");
  
  protected static void sendJson(Object o)
    throws IOException
  {
    response.contentType = "application/json";
    mapper.writeValue(response.out, o);
  }
  
  protected static Object readJson(Class<?> klass)
    throws IOException
  {
    return mapper.readValue(request.body, klass);
  }
  
  protected static boolean isJson()
  {
    return jsonPattern.matcher(request.contentType).matches();
  }
  
  protected static boolean isXml()
  {
    return xmlPattern.matcher(request.contentType).matches();
  }
  
  protected static boolean isText()
  {
    return textPattern.matcher(request.contentType).matches();
  }
  
  protected static boolean isForm()
  {
    return formPattern.matcher(request.contentType).matches();
  }
}
