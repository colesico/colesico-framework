package colesico.framework.example.web.pebble;

import colesico.framework.config.Config;
import colesico.framework.resource.ResourceOptionsPrototype;

@Config
public class ResourceConf extends ResourceOptionsPrototype {
    @Override
    public void bindProperties(PropertiesBinder binder) {
        binder.bind("$tmplRoot","colesico/framework/example/web/pebble/tmpl");
    }
}
