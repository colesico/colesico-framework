package colesico.framework.assist.codegen.model;

import colesico.framework.assist.StrUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

abstract public class VarElement extends ParserElement {

    public VarElement(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    abstract public String getName();

    public String getNameWithPrefix(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return getName();
        }
        return StrUtils.addPrefix(prefix, getName());
    }

    abstract public TypeMirror asType();

    public ClassType asClassType() {
        if (asType().getKind() == TypeKind.DECLARED) {
            return new ClassType(getProcessingEnv(), (DeclaredType) asType());

        }
        return null;
    }

}
