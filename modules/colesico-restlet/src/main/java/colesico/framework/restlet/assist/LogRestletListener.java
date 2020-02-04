package colesico.framework.restlet.assist;

import colesico.framework.http.HttpContext;
import colesico.framework.restlet.teleapi.RestletDataPort;
import colesico.framework.restlet.teleapi.RestletRequestListener;
import colesico.framework.restlet.teleapi.RestletResponseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.StringWriter;

@Singleton
public class LogRestletListener implements RestletRequestListener, RestletResponseListener {

    private static final Logger log = LoggerFactory.getLogger(LogRestletListener.class);

    @Override
    public void onRequest(HttpContext ctx, RestletDataPort dataPort, Object service) {
        if (!log.isDebugEnabled()) {
            return;
        }

        StringWriter out = new StringWriter();
        out.append("\n==== REQUEST BEGIN ====\n");
        ctx.getRequest().dump(out);
        out.append("\n==== REQUEST END ====\n");
        log.debug(out.toString());
    }

    @Override
    public void onResponse(HttpContext ctx, RestletDataPort dataPort) {
        if (!log.isDebugEnabled()) {
            return;
        }

        StringWriter out = new StringWriter();
        out.append("\n==== RESPONSE BEGIN ====\n");
        ctx.getResponse().dump(out);
        out.append("\n==== RESPONSE END ====\n");
        log.debug(out.toString());
    }
}
