package colesico.framework.beanvalidator.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.beanvalidator.Validate;
import colesico.framework.beanvalidator.ValidatorBuilderPrototype;
import colesico.framework.beanvalidator.ValidatorBuilderPrototypes;
import colesico.framework.beanvalidator.codegen.model.ValidatedBeanElement;
import colesico.framework.beanvalidator.codegen.model.ValidatedPropertyElement;
import colesico.framework.beanvalidator.codegen.model.ValidatorBuilderElement;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import java.util.List;

public class BeanValidationParser extends FrameworkAbstractParser {

    public static final String VALIDATOR_BUILDER_PROTOTYPE_SUFFIX = "ValidatorBuilder";
    public static final String VALIDATOR_BUILDER_PROTOTYPE_PREFIX = "Abstract";

    public BeanValidationParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected void parseValidatedFields(ValidatorBuilderElement builderElm) {
        logger.debug("Parse validated fields : " + builderElm);

        ClassElement beanClassElm = builderElm.getParentBean().getOriginType().asClassElement();
        List<FieldElement> fieldList = beanClassElm.getFieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        for (FieldElement fieldElm : fieldList) {
            logger.debug("Process validated field: {} of type {}", fieldElm.getName(), fieldElm.unwrap().asType());
            AnnotationAssist<Validate> validateAst = fieldElm.getAnnotation(Validate.class);
            if (validateAst == null) {
                continue;
            }
            ValidatedPropertyElement propertyElm = new ValidatedPropertyElement(fieldElm, validateAst.unwrap().asVerifier());
            builderElm.addProperty(propertyElm);
        }
    }

    protected void parseBuilder(ValidatedBeanElement beanElement, AnnotationAssist<ValidatorBuilderPrototype> builderAst) {

        String packageName = builderAst.unwrap().packageName();
        if (StringUtils.isBlank(packageName)) {
            packageName = beanElement.getOriginType().asClassElement().getPackageName();
        }

        String className = builderAst.unwrap().className();
        if (StringUtils.isBlank(className)) {
            className = VALIDATOR_BUILDER_PROTOTYPE_PREFIX + beanElement.getOriginType().asClassElement().getSimpleName() + VALIDATOR_BUILDER_PROTOTYPE_SUFFIX;
        }

        ValidatorBuilderElement builderElm = new ValidatorBuilderElement(
                packageName,
                className,
                new ClassType(processingEnv, (DeclaredType) builderAst.getValueTypeMirror(a -> a.extendsClass()))
        );

        beanElement.addBuilder(builderElm);

        parseValidatedFields(builderElm);
    }


    public ValidatedBeanElement parse(ClassElement beanClass) {
        ValidatedBeanElement builderElement = new ValidatedBeanElement(beanClass.asClassType());

        AnnotationAssist<ValidatorBuilderPrototype> builderAst = beanClass.getAnnotation(ValidatorBuilderPrototype.class);
        if (builderAst != null) {
            parseBuilder(builderElement, builderAst);
        } else {
            AnnotationAssist<ValidatorBuilderPrototypes> buildersAst = beanClass.getAnnotation(ValidatorBuilderPrototypes.class);
            if (buildersAst != null) {
                ValidatorBuilderPrototype[] buildersAnn = buildersAst.unwrap().value();
                for (ValidatorBuilderPrototype builderAnn : buildersAnn) {
                    builderAst = new AnnotationAssist<>(processingEnv, builderAnn);
                    parseBuilder(builderElement, builderAst);
                }
            } else {
                throw CodegenException.of().message("Annotation @" + ValidatorBuilderPrototype.class.getSimpleName() + " not specified").element(beanClass.unwrap()).build();
            }
        }

        return builderElement;
    }
}
