import javax.persistence.EntityManager;

import org.junit.*;

import com.apigee.apimodel.impl.APIImpl;
import com.apigee.apimodel.jpa.JPAWriter;

import play.test.*;
import play.db.jpa.JPA;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void testDatabaseLoad()
    {
      APIImpl api = new APIImpl();
      api.setName("TestAPI");
      api.setBasePath("http://localhost:8080");
      
      EntityManager em = JPA.em();
      
      JPAWriter writer = JPAWriter.get();
      writer.write(api, em);
    }
}