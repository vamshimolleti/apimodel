package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.codehaus.jackson.map.ObjectMapper;

import com.apigee.apimodel.impl.OrganizationImpl;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Router;
import play.mvc.results.Result;

public class Organizations
  extends GenericController
{
  @Transactional(readOnly=true)
  public static void getAll()
    throws IOException
  {
    Query q = JPA.em().createQuery("select x from com.apigee.apimodel.impl.OrganizationImpl x order by x.name");

    ArrayList<HashMap<String, Object>> json = new ArrayList<HashMap<String, Object>>();
    for (OrganizationImpl o : (List<OrganizationImpl>)q.getResultList()) {
      HashMap<String, Object> obj = new HashMap<String, Object>();
      obj.put("name", o.getName());
      HashMap<String, Object> args = new HashMap<String, Object>();
      args.put("org", o.getName());
      obj.put("link", Router.reverse("Organizations.getOne", args));
      json.add(obj);
    }
    sendJson(json);
    ok();
  }
  
  @Transactional(readOnly=true)
  public static void get(String org)
    throws IOException
  {
    OrganizationImpl o = JPA.em().find(OrganizationImpl.class, org);
    if (o == null) {
      notFound(org);
    } else {
      sendJson(o);
      ok();
    }
  }
  
  @Transactional
  public static void delete(String org)
  {
    OrganizationImpl o = JPA.em().find(OrganizationImpl.class, org);
    if (o == null) {
      notFound(org);
    } else {
      JPA.em().remove(o);
      ok();
    }
  }
  
  @Transactional
  public static void create(String format, String name)
    throws IOException
  {
    if (!isJson()) {
      error(400, "Bad content type");
    } else {
      Map<String, Object> m = (Map<String, Object>)readJson(Map.class);
      if (!m.containsKey("name")) {
        error(400, "Bad request");
      } else {
        OrganizationImpl org = new OrganizationImpl();
        org.setName((String)m.get("name"));
        JPA.em().persist(org);
        sendJson(org);
        ok();
      }
    }
  }
}
