package colesico.framework.example.web.localization;

import colesico.framework.config.Configuration;
import colesico.framework.resource.ResourceConfig;


@Configuration
public class ResourceConf extends ResourceConfig {

    @Override
    public void bindQualifiers(QualifiersBinder binder) {
        binder.bind(MyDictionary.BUNDLE_PATH,"L=ru","L=fr");
    }
}
