package colesico.framework.beanvalidation.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.beanvalidation.*;
import colesico.framework.beanvalidation.codegen.model.ValidateWithBuilderElement;
import colesico.framework.beanvalidation.codegen.model.ValidatedBeanElement;
import colesico.framework.beanvalidation.codegen.model.ValidatedPropertyElement;
import colesico.framework.beanvalidation.codegen.model.ValidatorBuilderElement;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import java.util.ArrayList;
import java.util.List;

public class BeanValidationParser extends FrameworkAbstractParser {

    public static final String VALIDATOR_BUILDER_PROTOTYPE_SUFFIX = "VBP";

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
            AnnotationAssist<ValidateWithBuilder> validateWithBuilderAst = fieldElm.getAnnotation(ValidateWithBuilder.class);
            if ((validateAst == null) && (validateWithBuilderAst == null)) {
                continue;
            }

            String subject = validateAst == null ? null : validateAst.unwrap().subject();

            if (StringUtils.isEmpty(subject)) {
                subject = fieldElm.getName();
            }

            String methodName = validateAst == null ? null : validateAst.unwrap().methodName();
            if (StringUtils.isEmpty(methodName)) {
                methodName = null;
            }

            boolean verifier = validateAst == null ? false : validateAst.unwrap().verifier();

            ValidateWithBuilderElement validateBean = null;
            if (validateWithBuilderAst != null) {
                ClassElement fieldBean = fieldElm.asClassType().asClassElement();
                List<AnnotationAssist<ValidatorBuilderPrototype>> builderProtsAst = getBuildersPrototypesAst(fieldBean);
                ValidatorBuilderElement fieldBuilderElm = createBuilderElement(fieldBean, builderProtsAst.get(0));
                String builderClassName = fieldBuilderElm.getPackageName() + '.' + fieldBuilderElm.getClassName();
                validateBean = new ValidateWithBuilderElement(builderClassName, validateWithBuilderAst.unwrap().optional());
            }

            ValidatedPropertyElement propertyElm = new ValidatedPropertyElement(fieldElm, subject, methodName, verifier, validateBean);
            builderElm.addProperty(propertyElm);
        }
    }

    protected ValidatorBuilderElement createBuilderElement(ClassElement beanElement, AnnotationAssist<ValidatorBuilderPrototype> builderProtAst) {
        DeclaredType extendsClass = (DeclaredType) builderProtAst.getValueTypeMirror(a -> a.extendsClass());
        DeclaredType packageFromClass = (DeclaredType) builderProtAst.getValueTypeMirror(a -> a.packageClass());

        String packageName = builderProtAst.unwrap().packageName();
        if (StringUtils.isBlank(packageName)) {
            if (!CodegenUtils.isAssignable(Class.class, packageFromClass, processingEnv)) {
                packageName = (new ClassType(processingEnv, packageFromClass)).asClassElement().getPackageName();
            } else if (!CodegenUtils.isAssignable(BeanValidatorBuilder.class, extendsClass, processingEnv)) {
                packageName = (new ClassType(processingEnv, extendsClass)).asClassElement().getPackageName();
            } else {
                packageName = beanElement.getPackageName();
            }
        }

        String className = builderProtAst.unwrap().className();
        if (StringUtils.isBlank(className)) {
            className = beanElement.getSimpleName() + VALIDATOR_BUILDER_PROTOTYPE_SUFFIX;
        }

        ValidatorBuilderElement builderElm = new ValidatorBuilderElement(
                packageName,
                className,
                new ClassType(processingEnv, extendsClass)
        );

        return builderElm;
    }

    protected void parseBuilder(ValidatedBeanElement beanElement, AnnotationAssist<ValidatorBuilderPrototype> builderProtAst) {
        ValidatorBuilderElement builderElm = createBuilderElement(beanElement.getOriginType().asClassElement(), builderProtAst);
        beanElement.addBuilder(builderElm);
        parseValidatedFields(builderElm);
    }

    protected List<AnnotationAssist<ValidatorBuilderPrototype>> getBuildersPrototypesAst(ClassElement beanClass) {
        List<AnnotationAssist<ValidatorBuilderPrototype>> result = new ArrayList<>();
        AnnotationAssist<ValidatorBuilderPrototype> builderProtAst = beanClass.getAnnotation(ValidatorBuilderPrototype.class);
        if (builderProtAst != null) {
            result.add(builderProtAst);
        } else {
            AnnotationAssist<ValidatorBuilderPrototypes> buildersAst = beanClass.getAnnotation(ValidatorBuilderPrototypes.class);
            if (buildersAst != null) {
                ValidatorBuilderPrototype[] buildersAnn = buildersAst.unwrap().value();
                for (ValidatorBuilderPrototype builderAnn : buildersAnn) {
                    builderProtAst = new AnnotationAssist<>(processingEnv, builderAnn);
                    result.add(builderProtAst);
                }
            } else {
                throw CodegenException.of().message("Annotation @" + ValidatorBuilderPrototype.class.getSimpleName() + " not specified").element(beanClass.unwrap()).build();
            }
        }
        return result;
    }

    public ValidatedBeanElement parse(ClassElement beanClass) {
        ValidatedBeanElement builderElm = new ValidatedBeanElement(beanClass.asClassType());
        List<AnnotationAssist<ValidatorBuilderPrototype>> builderProtsAst = getBuildersPrototypesAst(beanClass);
        for (AnnotationAssist<ValidatorBuilderPrototype> builderProtAst : builderProtsAst) {
            parseBuilder(builderElm, builderProtAst);
        }
        return builderElm;
    }
}
