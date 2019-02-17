package colesico.framework.assist.codegen.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Consumer;

public class AnnotationElement<A extends Annotation> extends Toolbox {

    protected final A originAnnotation;

    public AnnotationElement(ProcessingEnvironment processingEnv, A annotation) {
        super(processingEnv);
        this.originAnnotation = annotation;
    }

    public A unwrap() {
        return originAnnotation;
    }

    public TypeMirror getValueTypeMirror(Consumer<A> fieldAccessor) {
        TypeMirror typeMirror = null;
        try {
            fieldAccessor.accept(originAnnotation);
        } catch (MirroredTypeException mte) {
            typeMirror = mte.getTypeMirror();
        }
        return typeMirror;
    }

    public TypeMirror[] getValueTypeMirrors(Consumer<A> fieldAccessor) {
        try {
            fieldAccessor.accept(originAnnotation);
        } catch (javax.lang.model.type.MirroredTypesException mte) {
            List<? extends TypeMirror> typeMirrors = mte.getTypeMirrors();
            TypeMirror[] result = new TypeMirror[typeMirrors.size()];
            for (int i = 0; i < typeMirrors.size(); i++) {
                result[i] = typeMirrors.get(i);
            }
            return result;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotationElement<?> that = (AnnotationElement<?>) o;

        return originAnnotation != null ? originAnnotation.equals(that.originAnnotation) : that.originAnnotation == null;
    }

    @Override
    public int hashCode() {
        return originAnnotation != null ? originAnnotation.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AnnotationElement{" +
                "originAnnotation=" + originAnnotation +
                '}';
    }
}
