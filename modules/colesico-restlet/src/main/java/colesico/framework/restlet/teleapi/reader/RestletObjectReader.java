package colesico.framework.restlet.teleapi.reader;

import colesico.framework.restlet.teleapi.RestletTRContext;
import colesico.framework.restlet.teleapi.RestletTeleReader;
import colesico.framework.telehttp.reader.ObjectReader;

import javax.inject.Singleton;

/**
 * Http object reader proxy
 *
 * @see colesico.framework.telehttp.reader.ObjectReader
 */
@Singleton
public final class RestletObjectReader extends RestletTeleReader<Object> {

    private final ObjectReader reader;

    public RestletObjectReader(ObjectReader reader) {
        super(reader);
        this.reader = reader;
    }

    @Override
    public Object read(RestletTRContext context) {
        return reader.read(context);
    }

}
