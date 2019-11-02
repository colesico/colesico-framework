package colesico.framework.test.example.restlet;

import colesico.framework.example.restlet.User;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.undertow.HttpServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class RestletExampleTest {
    private Ioc ioc;
    private HttpServer httpServer;
    private HttpClient httpClient;
    private Gson gson = new Gson();
    private Logger logger = LoggerFactory.getLogger(RestletExampleTest.class);

    @BeforeClass
    public void init() {
        ioc = IocBuilder.forTests();

        httpServer = ioc.instance(HttpServer.class).start();

        httpClient = HttpClientBuilder.create().build();
    }

    @AfterClass
    public void destroy() {
        httpServer.stop();
    }

    private String requestGET(String url) throws IOException {
        var request = new HttpGet(url);
        request.setHeader("X-Requested-With", "XMLHttpRequest");
        String jsonText = EntityUtils.toString(httpClient.execute(request).getEntity(), StandardCharsets.UTF_8);
        return jsonText;
    }

    private String requestPOST(String url, String jsonRequest ) throws IOException {
        var request = new HttpPost(url);
        request.setHeader("X-Requested-With", "XMLHttpRequest");
        request.setEntity(new StringEntity(jsonRequest));
        String jsonText = EntityUtils.toString(httpClient.execute(request).getEntity(), StandardCharsets.UTF_8);
        return jsonText;
    }

    @Test
    public void test1() throws IOException {
        List<User> users = gson.fromJson(requestGET("http://localhost:8085/rest-api/list"), new TypeToken<List<User>>() {
        }.getType());
        assertEquals(users.get(0).getName(), "Ivan");
        assertEquals(users.get(1).getName(), "John");
    }

    @Test
    public void test2() throws IOException {
        User user = gson.fromJson(requestGET("http://localhost:8085/rest-api/find?id=1"), (Type) User.class);
        assertEquals(user.getName(), "Katherine");
        assertEquals(user.getId().longValue(), 1L);
    }

    @Test
    public void test3() throws IOException {
        User user = new User();
        user.setId(2L);
        user.setName("AName");
        Long id = gson.fromJson(requestPOST("http://localhost:8085/rest-api/save",gson.toJson(user)), Long.class);
        assertEquals(id.longValue(), 2L);
    }

}
