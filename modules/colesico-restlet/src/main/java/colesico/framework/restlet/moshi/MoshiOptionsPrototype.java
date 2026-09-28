package colesico.framework.restlet.moshi;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import com.squareup.moshi.Moshi;

/**
 * Default json converter tuning options
 */
@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class MoshiOptionsPrototype {
    abstract public void configure(Moshi.Builder builder);
}
