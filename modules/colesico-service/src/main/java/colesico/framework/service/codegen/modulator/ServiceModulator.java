package colesico.framework.service.codegen.modulator;

import colesico.framework.assist.CollectionUtils;
import colesico.framework.service.Service;
import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.Set;

@AutoService(Modulator.class)
public class ServiceModulator extends Modulator {

    @Override
    public Set<Class<? extends Annotation>> serviceAnnotations() {
        return CollectionUtils.annotationClassSet(Service.class);
    }
}
