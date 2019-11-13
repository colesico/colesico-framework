package colesico.framework.httpserver.internal;

import colesico.framework.httpserver.DefaultErrorHandler;
import colesico.framework.httpserver.ErrorHandler;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

import static colesico.framework.ioc.Rank.RANK_MINOR;

@Producer(RANK_MINOR)
@Produce(DefaultErrorHandler.class)
public class HttpServerProducer {
    public ErrorHandler getDefaultErrorHandler(DefaultErrorHandler impl) {
        return impl;
    }
}
