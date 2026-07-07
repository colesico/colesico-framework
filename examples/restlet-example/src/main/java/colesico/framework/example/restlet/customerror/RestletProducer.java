package colesico.framework.example.restlet.customerror;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.restlet.RestletWriter;

import jakarta.inject.Singleton;

/**
 * Producer to  register custom exception writer
 */
@Producer
@Produce(CustomExceptionWriter.class)
public class RestletProducer {

    @Singleton
    @Classed(CustomException.class)
    public RestletWriter customExceptionWriter(CustomExceptionWriter impl) {
        return impl;
    }

}
