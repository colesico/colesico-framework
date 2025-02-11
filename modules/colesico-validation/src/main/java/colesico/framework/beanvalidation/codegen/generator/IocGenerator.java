package colesico.framework.beanvalidation.codegen.generator;

import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.beanvalidation.codegen.model.ValidatorBuilderElement;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.ioc.production.Produce;
import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
import jakarta.inject.Singleton;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IocGenerator extends FrameworkAbstractGenerator {

    public static final String PRODUCER_SIMPLE_NAME = "ValidationBuildersProducer";

    public IocGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(List<ValidatorBuilderElement> validatorBuilders) {
        if (validatorBuilders.isEmpty()) {
            return;
        }

        Map<String, List<ValidatorBuilderElement>> byPackage = new HashMap<>();
        for (ValidatorBuilderElement vb : validatorBuilders) {
            List<ValidatorBuilderElement> vbs = byPackage.computeIfAbsent(vb.getPackageName(), k -> new ArrayList<>());
            vbs.add(vb);
        }

        for (Map.Entry<String, List<ValidatorBuilderElement>> entry : byPackage.entrySet()) {
            String packageName = entry.getKey();
            String producerClassSimpleName = PRODUCER_SIMPLE_NAME;
            ProducerGenerator producerGenerator = new ProducerGenerator(packageName, producerClassSimpleName, this.getClass(), processingEnv);
            List<ValidatorBuilderElement> vbs = entry.getValue();
            for (ValidatorBuilderElement vb : vbs) {
                logger.debug("Generating validator builders  producer: " + producerGenerator.getProducerClassFilePath());
                AnnotationSpec.Builder produceAnn = producerGenerator.addProduceAnnotation(TypeName.get(vb.getOriginClass().asClassType().unwrap()));
                TypeName keyType = TypeName.get(vb.getPrototypeType().unwrap());
                produceAnn.addMember(Produce.KEY_TYPE_METHOD, "$T.class", keyType);
                produceAnn.addMember(Produce.SCOPED_METHOD, "$T.class", ClassName.get(Singleton.class));

            }
            producerGenerator.generate();
        }
    }
}