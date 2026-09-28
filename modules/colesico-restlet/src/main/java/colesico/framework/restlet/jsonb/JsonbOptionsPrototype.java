package colesico.framework.restlet.jsonb;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import io.avaje.jsonb.Jsonb;

/**
 * Default json converter tuning options
 */
@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class JsonbOptionsPrototype {

    abstract public void configure(Jsonb.Builder builder);

}
