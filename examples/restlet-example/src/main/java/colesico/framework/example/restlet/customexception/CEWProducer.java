package colesico.framework.example.restlet.customexception;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.restlet.teleapi.RestletTeleWriter;

import javax.inject.Singleton;

/**
 * Producer to register custom exception writer
 */
@Producer
@Produce(CustomExceptionWriter.class)
public class CEWProducer {

    @Singleton
    @Classed(CustomException.class)
    public RestletTeleWriter getMyExceptionWriter(CustomExceptionWriter impl) {
        return impl;
    }

}
