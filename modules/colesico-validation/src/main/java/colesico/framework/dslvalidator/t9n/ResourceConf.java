package colesico.framework.dslvalidator.t9n;

import colesico.framework.config.Configuration;
import colesico.framework.resource.ResourceConfig;
import colesico.framework.translation.TranslationKit;

@Configuration
public class ResourceConf extends ResourceConfig {
    @Override
    public void bindQualifiers(QualifiersBinder binder) {
        binder.bind(TranslationKit.toBasePath(ValidatorMessages.class), "L=ru");
    }
}
