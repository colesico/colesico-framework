package colesico.framework.example.restlet.batchparam;

import colesico.framework.http.HttpMethod;
import colesico.framework.restlet.Restlet;
import colesico.framework.httprouter.RequestMethod;
import colesico.framework.service.BundleParam;
import colesico.framework.telehttp.ParamOrigin;
import colesico.framework.telehttp.origin.Origin;

import java.util.Map;

@Restlet
public class BatchParamApi {


    /**
     * POST: http://localhost:8080/batch-param-api/simple + data {"id":1,"name":"Vladlen", "val":"test" }
     */
    @RequestMethod(HttpMethod.POST)
    @BundleParam
    public Map<String, Object> simple(Long id,String name, String val) {
        return Map.of("id", id, "name", name, "val", val);
    }

    /**
     * Batch param mix with  query param
     * POST: http://localhost:8080/batch-param-api/mix?val=test + data {"id":1,"name":"Vladlen"}
     */
    @RequestMethod(HttpMethod.POST)
    public Map<String, Object> mix(
            @BundleParam("id")
            Long idValue,
            @BundleParam
            String name,
            @ParamOrigin(Origin.QUERY) String val) {
        return Map.of("id", idValue, "name", name, "val", val);
    }

}
