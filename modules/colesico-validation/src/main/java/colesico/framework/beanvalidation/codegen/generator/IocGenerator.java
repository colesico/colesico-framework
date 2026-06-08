package colesico.framework.beanvalidation.codegen.generator;

import colesico.framework.assist.StringUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.beanvalidation.codegen.model.ValidatorBuilderElement;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.scope.Unscoped;
import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IocGenerator extends FrameworkAbstractGenerator {

    public static final String PRODUCER_SIMPLE_NAME = "ValidationProducer";

    public IocGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(List<ValidatorBuilderElement> validatorBuilders) {
        if (validatorBuilders.isEmpty()) {
            return;
        }

        Map<String, List<ValidatorBuilderElement>> byPackage = new HashMap<>();
        for (ValidatorBuilderElement vb : validatorBuilders) {
            List<ValidatorBuilderElement> vbs = byPackage.computeIfAbsent(vb.packageName(), k -> new ArrayList<>());
            vbs.add(vb);
        }

        for (Map.Entry<String, List<ValidatorBuilderElement>> entry : byPackage.entrySet()) {
            String packageName = entry.getKey();
            String producerClassSimpleName = PRODUCER_SIMPLE_NAME;
            ProducerGenerator producerGenerator = new ProducerGenerator(packageName, producerClassSimpleName, this.getClass(), processingEnv);
            List<ValidatorBuilderElement> vbs = entry.getValue();
            for (ValidatorBuilderElement vb : vbs) {
                logger.debug("Generating validator builders  producer: " + producerGenerator.producerClassFilePath());
                TypeName builderType = TypeName.get(vb.originClass().asClassType().unwrap());
                AnnotationSpec.Builder produceAnn = producerGenerator.addProduceAnnotation(builderType);
                TypeName keyType = TypeName.get(vb.prototypeType().unwrap());
                produceAnn.addMember(Produce.SCOPED_METHOD, "$T.class", ClassName.get(Unscoped.class));
                String methodName = "get" + StringUtils.firstCharToUpperCase(vb.originClass().simpleName());
                MethodSpec.Builder mb = producerGenerator.addProduceMethod(methodName, keyType);
                mb.addAnnotation(Unscoped.class);
                mb.addParameter(builderType, "vb");
                mb.addStatement("return vb");
            }
            producerGenerator.generate();
        }
    }
}