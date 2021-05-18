package colesico.framework.telehttp.reader;

import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.OriginFactory;
import colesico.framework.telehttp.OriginReader;

import javax.inject.Inject;

/**
 * Performs custom object reading using reflection.
 * Object fields values are reading with appropriate http-readers if the field type can be reade by tye reader.
 * Otherwise field read as nested custom object
 */
abstract public class ObjectReader<C extends HttpTRContext> extends OriginReader<Object, C> {

    @Inject
    public ObjectReader(OriginFactory originFactory) {
        super(originFactory);
    }
}
