package colesico.framework.dslvalidator.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.dslvalidator.beanvalidation.ValidatorBuilderPrototype;
import colesico.framework.dslvalidator.beanvalidation.ValidatorBuilderPrototypes;
import colesico.framework.dslvalidator.codegen.model.ValidatedBeanElement;
import colesico.framework.dslvalidator.codegen.model.ValidatorBuilderPrototypeElement;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;

public class BeanValidationParser extends FrameworkAbstractParser {

    public static final String VALIDATOR_BUILDER_PROTOTYPE_SUFFIX = "VBP";

    public BeanValidationParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected void parseVBP(ValidatedBeanElement vbElement, AnnotationAssist<ValidatorBuilderPrototype> vbpAnnElm) {

        String packageName = vbpAnnElm.unwrap().packageName();
        if (StringUtils.isBlank(packageName)) {
            packageName = vbElement.getOriginClass().getPackageName();
        }

        String className = vbpAnnElm.unwrap().className();
        if (StringUtils.isBlank(className)) {
            className = vbElement.getOriginClass().getSimpleName() + VALIDATOR_BUILDER_PROTOTYPE_SUFFIX;
        }

        ValidatorBuilderPrototypeElement vbpElm = new ValidatorBuilderPrototypeElement(
                packageName,
                className,
                new ClassType(processingEnv, (DeclaredType) vbpAnnElm.getValueTypeMirror(a -> a.extendsClass()))
        );

        vbElement.addBuilderPrototype(vbpElm);
    }


    public ValidatedBeanElement parse(ClassElement beanClass) {
        ValidatedBeanElement vbElement = new ValidatedBeanElement(beanClass);

        AnnotationAssist<ValidatorBuilderPrototype> vbpAnnElm = beanClass.getAnnotation(ValidatorBuilderPrototype.class);
        if (vbpAnnElm != null) {
            parseVBP(vbElement, vbpAnnElm);
        } else {
            AnnotationAssist<ValidatorBuilderPrototypes> vbpsAnnElm = beanClass.getAnnotation(ValidatorBuilderPrototypes.class);
            if (vbpsAnnElm != null) {
                ValidatorBuilderPrototype[] avpAnns = vbpsAnnElm.unwrap().value();
                for (ValidatorBuilderPrototype avpAnn : avpAnns) {
                    vbpAnnElm = new AnnotationAssist<>(processingEnv, avpAnn);
                    parseVBP(vbElement, vbpAnnElm);
                }
            } else {
                throw CodegenException.of().message("Annotation @" + ValidatorBuilderPrototype.class.getSimpleName() + " not specified").element(beanClass.unwrap()).build();
            }
        }

        return vbElement;
    }
}
