package colesico.framework.restlet.teleapi.jsonmap;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

public final class JsonMapContext {
    
    public static final Key<JsonMapContext> SCOPE_KEY = new TypeKey<>(JsonMapContext.class);

    private final JsonMap valuesMap;

    public JsonMapContext(JsonMap valuesMap) {
        this.valuesMap = valuesMap;
    }

    public <V> V getValue(String name) {
        return valuesMap.getValue(name);
    }
}
