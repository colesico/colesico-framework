package colesico.framework.assist.codegen.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import java.util.HashMap;
import java.util.Map;

public class AnnotationMirrorElement extends ParserElement {

    protected final AnnotationMirror originAnnotationMirror;

    public AnnotationMirrorElement(ProcessingEnvironment processingEnv, AnnotationMirror annotationMirror) {
        super(processingEnv);
        this.originAnnotationMirror = annotationMirror;
    }

    @Override
    public Element unwrap() {
        return originAnnotationMirror.getAnnotationType().asElement();
    }

    public DeclaredType getType() {
        return originAnnotationMirror.getAnnotationType();
    }

    public AnnotationValue getValue(String fieldName) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : originAnnotationMirror.getElementValues().entrySet()) {
            if (fieldName.equals(entry.getKey().getSimpleName().toString())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Map<? extends ExecutableElement, ? extends AnnotationValue> getValuesWithDefaults() {
        Map<ExecutableElement, AnnotationValue> valMap = new HashMap<>();
        if (originAnnotationMirror.getElementValues() != null) {
            valMap.putAll(originAnnotationMirror.getElementValues());
        }
        for (ExecutableElement meth : ElementFilter.methodsIn(originAnnotationMirror.getAnnotationType().asElement().getEnclosedElements())) {
            AnnotationValue defaultValue = meth.getDefaultValue();
            if (defaultValue != null && !valMap.containsKey(meth)) {
                valMap.put(meth, defaultValue);
            }
        }
        return valMap;
    }
}
