package colesico.framework.example.restlet;

import colesico.framework.http.HttpMethod;
import colesico.framework.restlet.Restlet;
import colesico.framework.router.RequestMethod;
import colesico.framework.router.Route;

import java.util.Arrays;
import java.util.List;

/**
 * Mandatory http header X-Requested-With:XMLHttpRequest
 */
@Restlet
public class RestApi {

    // GET http://localhost:8080/rest-api/list
    public List<User> list() {
        return Arrays.asList(new User(1L, "Ivan"), new User(2L, "John"));
    }

    // GET http://localhost:8080/rest-api/find?id=1
    @Route("find")
    public User get(Long id) {
        return new User(id,"Katherine");
    }

    // POST  http://localhost:8080/rest-api/save +  data {"id":1,"name":"Anne"}
    @RequestMethod(HttpMethod.POST)
    public Long save(User user){
        return user.getId();
    }

}
