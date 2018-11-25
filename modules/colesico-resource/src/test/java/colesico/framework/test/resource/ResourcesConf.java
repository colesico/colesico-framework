package colesico.framework.test.resource;

import colesico.framework.resource.ResourceConfig;

public class ResourcesConf extends ResourceConfig {

    @Override
    public void bindProperties(PropertiesBinder binder) {
        binder.bind("$alias", "foo/dummy");
    }

}
