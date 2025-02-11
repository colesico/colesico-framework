package colesico.framework.beanvalidation.codegen.generator;

import colesico.framework.assist.codegen.FrameworkAbstractGenerator;

import javax.annotation.processing.ProcessingEnvironment;

public class IocGenerator extends FrameworkAbstractGenerator {

    public static final String PRODUCER_SUFFIX = "Producer";

    public IocGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }
}
