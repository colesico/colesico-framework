package colesico.framework.restlet.teleapi.reader;

import colesico.framework.restlet.RestletError;
import colesico.framework.restlet.RestletException;
import colesico.framework.restlet.teleapi.RestletTRContext;
import colesico.framework.restlet.teleapi.jsonmap.JsonEntry;
import colesico.framework.restlet.teleapi.jsonmap.JsonMapContext;

import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @see JsonEntry
 */
@Singleton
public final class JsonEntryReader implements ValueReader {

    private final Provider<JsonMapContext> jsonMapCtxProv;

    public JsonEntryReader(Provider<JsonMapContext> jsonMapCtxProv) {
        this.jsonMapCtxProv = jsonMapCtxProv;
    }

    @Override
    public Object read(RestletTRContext context) {
        JsonMapContext ctx = jsonMapCtxProv.get();
        if (ctx == null) {
            throw new RestletException(new RestletError("JsonMapContextNotFound", "Json map context not found", null));
        }
        return ctx.getValue(context.getName());
    }

}
