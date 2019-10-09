package colesico.framework.example.web.localization;

import colesico.framework.config.Config;
import colesico.framework.resource.ResourceOptionsPrototype;


@Config
public class ResourceConf extends ResourceOptionsPrototype {

    @Override
    public void bindQualifiers(QualifiersBinder binder) {
        binder.bind(MyDictionary.BUNDLE_PATH,"L=ru","L=fr");
    }
}
