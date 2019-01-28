package colesico.framework.service.codegen.modulator;

import colesico.framework.assist.CollectionUtils;
import colesico.framework.service.Service;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ServiceModulator extends Modulator {

    @Override
    public Set<Class<? extends Annotation>> serviceAnnotations() {
        return CollectionUtils.annotationClassSet(Service.class);
    }
}
