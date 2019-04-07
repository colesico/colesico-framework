package colesico.framework.restlet.teleapi.gson;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import com.google.gson.GsonBuilder;

/**
 * Default json converter tuning options
 */
@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class RestletGsonOptions {
    abstract public void applyOptions(GsonBuilder builder);
}
