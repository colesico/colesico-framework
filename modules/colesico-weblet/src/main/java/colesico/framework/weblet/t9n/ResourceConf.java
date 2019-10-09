package colesico.framework.weblet.t9n;

import colesico.framework.config.Config;
import colesico.framework.resource.ResourceOptionsPrototype;
import colesico.framework.translation.TranslationKit;

@Config
public class ResourceConf extends ResourceOptionsPrototype {

    @Override
    public void bindQualifiers(QualifiersBinder binder) {
        binder.bind(TranslationKit.toBasePath(WebletMessages.class),"L=ru");
    }
}
