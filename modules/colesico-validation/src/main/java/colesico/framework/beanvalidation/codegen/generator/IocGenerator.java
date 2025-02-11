package colesico.framework.beanvalidation.codegen.generator;

import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.beanvalidation.codegen.model.ValidatorBuilderElement;

import javax.annotation.processing.ProcessingEnvironment;

public class IocGenerator extends FrameworkAbstractGenerator {

    public static final String PRODUCER_SUFFIX = "Producer";

    public IocGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(ValidatorBuilderElement validatorBilder) {

    }
}