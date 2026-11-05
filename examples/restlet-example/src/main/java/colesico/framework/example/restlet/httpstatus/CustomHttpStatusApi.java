package colesico.framework.example.restlet.httpstatus;

import colesico.framework.example.restlet.helloworld.User;
import colesico.framework.httprouter.Route;
import colesico.framework.restlet.Restlet;
import colesico.framework.telehttp.result.ValueResult;

@Restlet
@Route("http-status")
public class CustomHttpStatusApi {

    /**
     * GET http://localhost:8080/http-status/find?id=1
     */
    @Route("find")
    public ValueResult<User> get(long id) {
        return ValueResult
                .value(new User(id, "Http204"))
                .status(204)
                .build();
    }

}
