package colesico.framework.example.web.pebble;

import colesico.framework.config.Configuration;
import colesico.framework.resource.ResourceConfig;

@Configuration
public class ResourceConf extends ResourceConfig {
    @Override
    public void bindProperties(PropertiesBinder binder) {
        binder.bind("$tmplRoot","colesico/framework/example/web/pebble/tmpl");
    }
}
