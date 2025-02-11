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
import colesico.framework.beanvalidation.codegen.model.PropertyValidateElement;
import colesico.framework.beanvalidation.codegen.model.ValidatorBuilderPrototypeElement;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import java.util.*;

public class ValidatedBeanParser extends FrameworkAbstractParser {

    public ValidatedBeanParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected void parsePropertyValidation(ValidatorBuilderPrototypeElement validatorBuilder,
                                           FieldElement field,
                                           AnnotationAssist<Validate> validateSpec) {

        Set<String> builders = new HashSet<>(Arrays.asList(validateSpec.unwrap().builders()));
        if (!builders.contains(validatorBuilder.getName())) {
            return;
        }

        String subject = validateSpec.unwrap().subject();
        if (StringUtils.isEmpty(subject)) {
            subject = field.getName();
        }

        boolean verifier = validateSpec == null ? false : validateSpec.unwrap().verifier();

        String mapper = validateSpec.unwrap().mapper();

        PropertyValidateElement propertyValidation = new PropertyValidateElement(field, subject, mapper, verifier);
        validatorBuilder.addValidation(propertyValidation);
    }

    protected void parseBeanValidation(ValidatorBuilderPrototypeElement validatorBuilder,
                                       FieldElement field,
                                       AnnotationAssist<ValidateBean> validateBeanSpec) {

        Set<String> builders = new HashSet<>(Arrays.asList(validateBeanSpec.unwrap().builders()));
        if (!builders.contains(validatorBuilder.getName())) {
            return;
        }

        String targetBuilder = validateBeanSpec.unwrap().targetBuilder();

        ClassElement targetBeanClass = field.asClassType().asClassElement();
        List<AnnotationAssist<ValidatorBuilderPrototype>> targetBuilderSpecList = getBuilderSpecs(targetBeanClass);
        AnnotationAssist<ValidatorBuilderPrototype> targetBuilderSpec = null;
        for (AnnotationAssist<ValidatorBuilderPrototype> tbs : targetBuilderSpecList) {
            if (targetBuilder.equals(tbs.unwrap().name())) {
                targetBuilderSpec = tbs;
            }
        }
        if (targetBuilderSpec == null) {
            throw CodegenException.of()
                    .message("Validation target builder not found: " + targetBuilder)
                    .element(field.unwrap())
                    .build();
        }

        ValidatorBuilderPrototypeElement fieldValidatorBuilder = createValidatorBuilderElement(targetBeanClass, targetBuilderSpec);
        BeanElement validatedBean = new BeanElement(targetBeanClass.asClassType());
        validatedBean.addValidatorBuilder(fieldValidatorBuilder);

        String subject = validateBeanSpec.unwrap().subject();
        if (StringUtils.isEmpty(subject)) {
            subject = field.getName();
        }

        String mapper = validateBeanSpec.unwrap().mapper();

        BeanValidateElement beanValidation = new BeanValidateElement(field, subject, mapper, fieldValidatorBuilder);

        validatorBuilder.addValidation(beanValidation);
    }

    protected void parseFieldsValidations(ValidatorBuilderPrototypeElement validatorBuilder) {
        logger.debug("Parse fields validations : " + validatorBuilder);

        ClassElement beanClass = validatorBuilder.getParentBean().getOriginType().asClassElement();
        List<FieldElement> fieldsList = beanClass.getFieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        for (FieldElement field : fieldsList) {
            logger.debug("Process bean field: {} of type {}", field.getName(), field.unwrap().asType());
            AnnotationAssist<Validate> validateSpec = field.getAnnotation(Validate.class);
            if (validateSpec != null) {
                parsePropertyValidation(validatorBuilder, field, validateSpec);
            } else {
                AnnotationAssist<ValidateBean> validateBeanSpec = field.getAnnotation(ValidateBean.class);
                if (validateBeanSpec != null) {
                    parseBeanValidation(validatorBuilder, field, validateBeanSpec);
                }
            }
        }
    }

    protected ValidatorBuilderPrototypeElement createValidatorBuilderElement(ClassElement beanClass, AnnotationAssist<ValidatorBuilderPrototype> builderSpec) {

        DeclaredType superclass = (DeclaredType) builderSpec.getValueTypeMirror(a -> a.superclass());
        String packageName = builderSpec.unwrap().packageName();

        if (StringUtils.isBlank(packageName)) {
            if (!CodegenUtils.isAssignable(BeanValidatorBuilder.class, superclass, processingEnv)) {
                packageName = (new ClassType(processingEnv, superclass)).asClassElement().getPackageName();
            } else {
                packageName = beanClass.getPackageName();
            }
        }

        String name = builderSpec.unwrap().name();
        if (StringUtils.isBlank(name)) {
            name = ValidatorBuilderPrototype.DEFAULT_BUILDER;
        }

        String command = builderSpec.unwrap().command();

        ValidatorBuilderPrototypeElement validatorBuilder = new ValidatorBuilderPrototypeElement(name, packageName,
                ClassType.of(processingEnv, superclass), command);

        return validatorBuilder;
    }

    protected void parseValidatedBean(BeanElement validatedBean, AnnotationAssist<ValidatorBuilderPrototype> builderSpec) {
        ValidatorBuilderPrototypeElement validatorBuilder = createValidatorBuilderElement(validatedBean.getOriginType().asClassElement(), builderSpec);
        validatedBean.addValidatorBuilder(validatorBuilder);
        parseFieldsValidations(validatorBuilder);
    }

    protected List<AnnotationAssist<ValidatorBuilderPrototype>> getBuilderSpecs(ClassElement beanClass) {
        List<AnnotationAssist<ValidatorBuilderPrototype>> result = new ArrayList<>();
        AnnotationAssist<ValidatorBuilderPrototype> builderSpec = beanClass.getAnnotation(ValidatorBuilderPrototype.class);
        if (builderSpec != null) {
            result.add(builderSpec);
        } else {
            AnnotationAssist<ValidatorBuilderPrototypes> buildersSpec = beanClass.getAnnotation(ValidatorBuilderPrototypes.class);
            if (buildersSpec != null) {
                ValidatorBuilderPrototype[] builderSpecArr = buildersSpec.unwrap().value();
                for (ValidatorBuilderPrototype builderAnn : builderSpecArr) {
                    builderSpec = new AnnotationAssist<>(processingEnv, builderAnn);
                    result.add(builderSpec);
                }
            } else {
                throw CodegenException.of().message("Annotation @" + ValidatorBuilderPrototype.class.getSimpleName() + " not specified").element(beanClass.unwrap()).build();
            }
        }
        return result;
    }

    public BeanElement parse(ClassElement beanClass) {
        BeanElement validatedBean = new BeanElement(beanClass.asClassType());
        List<AnnotationAssist<ValidatorBuilderPrototype>> builderSpecList = getBuilderSpecs(beanClass);
        for (AnnotationAssist<ValidatorBuilderPrototype> builderSpec : builderSpecList) {
            parseValidatedBean(validatedBean, builderSpec);
        }
        return validatedBean;
    }
}
