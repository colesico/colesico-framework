package colesico.framework.test.resource;

import colesico.framework.resource.ResourceOptionsPrototype;

public class ResourcesConf extends ResourceOptionsPrototype {

    @Override
    public void bindProperties(PropertiesBinder binder) {
        binder.bind("$alias", "foo/dummy");
    }

}
