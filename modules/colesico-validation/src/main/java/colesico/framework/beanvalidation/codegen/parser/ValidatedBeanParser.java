package colesico.framework.beanvalidation.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.beanvalidation.*;
import colesico.framework.beanvalidation.codegen.model.BeanElement;
import colesico.framework.beanvalidation.codegen.model.BeanValidateElement;
import colesico.framework.beanvalidation.codegen.model.ValueValidateElement;
import colesico.framework.beanvalidation.codegen.model.ValidatorBuilderElement;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import java.util.*;

import static colesico.framework.assist.StringUtils.isBlank;

public class ValidatedBeanParser extends FrameworkAbstractParser {

    public ValidatedBeanParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    private boolean isBuilderUnaccepted(ValidatorBuilderElement validatorBuilder, DeclaredType builderClass) {
        if (!CodegenUtils.isAssignable(BeanValidatorBuilder.class, builderClass, processingEnv)) {
            return !typeUtils().isSameType(builderClass, validatorBuilder.superclass().unwrap());
        } else {
            return !validatorBuilder.isDefault();
        }
    }

    protected void parseValueValidation(ValidatorBuilderElement validatorBuilder,
                                        FieldElement field,
                                        List<AnnotationAssist<Validate>> validateSpecs) {

        for (AnnotationAssist<Validate> validateSpec : validateSpecs) {


            DeclaredType builderClass = (DeclaredType) validateSpec.valueTypeMirror(a -> a.value());
            // if specified builder class
            if (isBuilderUnaccepted(validatorBuilder, builderClass)) {
                continue;
            }

            String method = validateSpec.unwrap().method();
            if (isBlank(method)) {
                method = null;
            }

            String subject = validateSpec.unwrap().subject();
            if (isBlank(subject)) {
                subject = field.name();
            }

            boolean verifier = validateSpec.unwrap().verifier();

            String mapper = validateSpec.unwrap().mapper();

            ValueValidateElement valueValidation = new ValueValidateElement(field, subject, mapper, method, verifier);
            validatorBuilder.addValidation(valueValidation);
        }
    }

    protected void parseBeanValidation(ValidatorBuilderElement validatorBuilder,
                                       FieldElement field,
                                       List<AnnotationAssist<BeanValidate>> validateSpecs) {

        for (AnnotationAssist<BeanValidate> validateSpec : validateSpecs) {

            DeclaredType builderClass = (DeclaredType) validateSpec.valueTypeMirror(a -> a.value());
            // if specified builder class
            if (isBuilderUnaccepted(validatorBuilder, builderClass)) {
                continue;
            }

            DeclaredType targetBuilderType = (DeclaredType) validateSpec.valueTypeMirror(a -> a.target());
            boolean isDefaultTargetBuilder = CodegenUtils.isAssignable(BeanValidatorBuilder.class, targetBuilderType, processingEnv());

            ClassElement targetBeanClass = field.asClassType().asClassElement();
            List<AnnotationAssist<ValidatorBuilder>> targetBuilderSpecList = getBuilderSpecs(targetBeanClass);
            AnnotationAssist<ValidatorBuilder> targetBuilderSpec = null;
            for (AnnotationAssist<ValidatorBuilder> tbs : targetBuilderSpecList) {
                if (isDefaultTargetBuilder && tbs.unwrap().isDefault()) {
                    targetBuilderSpec = tbs;
                    break;
                } else {
                    DeclaredType tbsSuperclass = getBuilderSuperclass(tbs);
                    if (typeUtils().isSameType(targetBuilderType, tbsSuperclass)) {
                        targetBuilderSpec = tbs;
                        break;
                    }
                }
            }

            if (targetBuilderSpec == null) {
                throw CodegenException.of()
                        .message("Target validator builder not found: " + targetBuilderType)
                        .element(field.unwrap())
                        .build();
            }

            ValidatorBuilderElement fieldValidatorBuilder = createValidatorBuilderElement(targetBeanClass, targetBuilderSpec);
            BeanElement validatedBean = new BeanElement(targetBeanClass.asClassType());
            validatedBean.addValidatorBuilder(fieldValidatorBuilder);

            String subject = validateSpec.unwrap().subject();
            if (isBlank(subject)) {
                subject = field.name();
            }

            String mapper = validateSpec.unwrap().mapper();

            BeanValidateElement beanValidation = new BeanValidateElement(field, subject, mapper, fieldValidatorBuilder);

            validatorBuilder.addValidation(beanValidation);
        }
    }

    protected void parseFieldsValidations(ValidatorBuilderElement validatorBuilder) {
        logger.debug("Parse fields validations : " + validatorBuilder);

        ClassElement beanClass = validatorBuilder.parentBean().originType().asClassElement();
        List<FieldElement> fieldsList = beanClass.fieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        for (FieldElement field : fieldsList) {
            logger.debug("Process bean field: {} of type {}", field.name(), field.unwrap().asType());
            List<AnnotationAssist<Validate>> validateValueSpecs = getValidateSpecs(field);
            if (!validateValueSpecs.isEmpty()) {
                parseValueValidation(validatorBuilder, field, validateValueSpecs);
            } else {
                List<AnnotationAssist<BeanValidate>> beanValidateSpecs = getBeanValidateSpecs(field);
                if (beanValidateSpecs != null) {
                    parseBeanValidation(validatorBuilder, field, beanValidateSpecs);
                }
            }
        }
    }

    protected ValidatorBuilderElement createValidatorBuilderElement(ClassElement beanClass,
                                                                    AnnotationAssist<ValidatorBuilder> builderSpec) {

        DeclaredType superclass = getBuilderSuperclass(builderSpec);

        boolean isDefault = builderSpec.unwrap().isDefault();

        String packageName = builderSpec.unwrap().packageName();
        if (isBlank(packageName)) {
            packageName = (new ClassType(processingEnv, superclass)).asClassElement().packageName();
        }

        String subject = builderSpec.unwrap().subject();
        if (isBlank(subject)) {
            subject = null;
        }

        String command = builderSpec.unwrap().command();

        return new ValidatorBuilderElement(
                ClassType.of(processingEnv, superclass),
                isDefault, packageName, subject, command);
    }

    private DeclaredType getBuilderSuperclass(AnnotationAssist<ValidatorBuilder> builderSpec) {
        DeclaredType superclass = (DeclaredType) builderSpec.valueTypeMirror(a -> a.superclass());
        if (CodegenUtils.isAssignable(BeanValidatorBuilder.class, superclass, processingEnv)) {
            superclass = (DeclaredType) builderSpec.valueTypeMirror(a -> a.value());
        }
        return superclass;
    }

    protected void parseValidatedBean(BeanElement validatedBean, AnnotationAssist<ValidatorBuilder> builderSpec) {
        ValidatorBuilderElement validatorBuilder = createValidatorBuilderElement(validatedBean.originType().asClassElement(), builderSpec);
        validatedBean.addValidatorBuilder(validatorBuilder);
        if (validatorBuilder.isDefault()) {
            validatedBean.setDefaultValidatorBuilder(validatorBuilder);
        }
        parseFieldsValidations(validatorBuilder);
    }

    protected List<AnnotationAssist<Validate>> getValidateSpecs(FieldElement field) {
        List<AnnotationAssist<Validate>> result = new ArrayList<>();
        AnnotationAssist<Validate> validateSpec = field.annotation(Validate.class);
        if (validateSpec != null) {
            result.add(validateSpec);
        } else {
            AnnotationAssist<Validates> validateSpecs = field.annotation(Validates.class);
            if (validateSpecs != null) {
                Validate[] validateSpecArr = validateSpecs.unwrap().value();
                for (Validate validateAnn : validateSpecArr) {
                    validateSpec = new AnnotationAssist<>(processingEnv, validateAnn);
                    result.add(validateSpec);
                }
            }
        }
        return result;
    }

    protected List<AnnotationAssist<BeanValidate>> getBeanValidateSpecs(FieldElement field) {
        List<AnnotationAssist<BeanValidate>> result = new ArrayList<>();
        AnnotationAssist<BeanValidate> validateSpec = field.annotation(BeanValidate.class);
        if (validateSpec != null) {
            result.add(validateSpec);
        } else {
            AnnotationAssist<BeanValidates> validateSpecs = field.annotation(BeanValidates.class);
            if (validateSpecs != null) {
                BeanValidate[] validateSpecArr = validateSpecs.unwrap().value();
                for (BeanValidate validateAnn : validateSpecArr) {
                    validateSpec = new AnnotationAssist<>(processingEnv, validateAnn);
                    result.add(validateSpec);
                }
            }
        }
        return result;
    }


    protected List<AnnotationAssist<ValidatorBuilder>> getBuilderSpecs(ClassElement beanClass) {
        List<AnnotationAssist<ValidatorBuilder>> result = new ArrayList<>();
        AnnotationAssist<ValidatorBuilder> builderSpec = beanClass.annotation(ValidatorBuilder.class);
        if (builderSpec != null) {
            result.add(builderSpec);
        } else {
            AnnotationAssist<ValidatorBuilders> builderSpecs = beanClass.annotation(ValidatorBuilders.class);
            if (builderSpecs != null) {
                ValidatorBuilder[] builderSpecArr = builderSpecs.unwrap().value();
                for (ValidatorBuilder builderAnn : builderSpecArr) {
                    builderSpec = new AnnotationAssist<>(processingEnv, builderAnn);
                    result.add(builderSpec);
                }
            } else {
                throw CodegenException.of().message("Annotation @" + ValidatorBuilder.class.getSimpleName() + " not specified").element(beanClass.unwrap()).build();
            }
        }
        return result;
    }


    public BeanElement parse(ClassElement validatedBeanClass) {
        BeanElement validatedBean = new BeanElement(validatedBeanClass.asClassType());
        List<AnnotationAssist<ValidatorBuilder>> builderSpecList = getBuilderSpecs(validatedBeanClass);
        for (AnnotationAssist<ValidatorBuilder> builderSpec : builderSpecList) {
            parseValidatedBean(validatedBean, builderSpec);
        }
        return validatedBean;
    }
}
