package colesico.framework.assist.codegen.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

abstract public class ParserType  extends Toolbox{

    public ParserType(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    abstract public TypeMirror unwrap();
}
