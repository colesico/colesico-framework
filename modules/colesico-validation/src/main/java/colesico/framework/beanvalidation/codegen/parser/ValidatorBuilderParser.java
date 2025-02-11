package colesico.framework.beanvalidation.codegen.parser;

import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.beanvalidation.codegen.model.ValidatorBuilderElement;

import javax.annotation.processing.ProcessingEnvironment;

public class ValidatorBuilderParser  extends FrameworkAbstractParser {
    public ValidatorBuilderParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public ValidatorBuilderElement parse(ClassElement validatorBuilderClass){
        return null;
    }
}
