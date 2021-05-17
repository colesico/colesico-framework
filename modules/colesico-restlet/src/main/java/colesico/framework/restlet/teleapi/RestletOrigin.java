package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.Origin;

public class RestletOrigin extends Origin {

    /**
     * Read param value from request object field.
     * The request object is read from the request body in json notation
     */
    public static final String FIELD = "FIELD";

    public static final Origin ORIGIN_FIELD = of(FIELD);

    protected RestletOrigin(String name) {
        super(name);
    }
}
