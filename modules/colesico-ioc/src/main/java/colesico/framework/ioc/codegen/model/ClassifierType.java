package colesico.framework.ioc.codegen.model;

import colesico.framework.assist.codegen.model.ParserType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

public class ClassifierType extends ParserType {
    private final TypeMirror originType;

    public ClassifierType(ProcessingEnvironment processingEnv, TypeMirror originType) {
        super(processingEnv);
        this.originType = originType;
    }

    @Override
    public TypeMirror unwrap() {
        return originType;
    }

    public TypeMirror getErasure() {
        return getTypeUtils().erasure(originType);
    }

}
