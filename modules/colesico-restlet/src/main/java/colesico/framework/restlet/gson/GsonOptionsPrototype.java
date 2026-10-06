package colesico.framework.restlet.gson;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import com.google.gson.GsonBuilder;

/**
 * Default json converter tuning options
 */
@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class GsonOptionsPrototype {
    abstract public GsonBuilder configure(GsonBuilder builder);
}