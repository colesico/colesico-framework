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
import colesico.framework.beanvalidation.codegen.model.BeanValidationElement;
import colesico.framework.beanvalidation.codegen.model.PropertyValidationElement;
import colesico.framework.beanvalidation.codegen.model.ValidatorBuilderElement;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import java.util.*;

public class BeanValidationParser extends FrameworkAbstractParser {

    public BeanValidationParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected void parsePropertyValidation(ValidatorBuilderElement validatorBuilder,
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

        String command = validateSpec.unwrap().command();

        PropertyValidationElement propertyValidation = new PropertyValidationElement(field, subject, command, verifier);
        validatorBuilder.addValidation(propertyValidation);
    }

    protected void parseBeanValidation(ValidatorBuilderElement validatorBuilder,
                                       FieldElement field,
                                       AnnotationAssist<ValidateBean> validateBeanSpec) {

        Set<String> builders = new HashSet<>(Arrays.asList(validateBeanSpec.unwrap().builders()));
        if (!builders.contains(validatorBuilder.getName())) {
            return;
        }

        String targetBuilder = validateBeanSpec.unwrap().targetBuilder();

        ClassElement targetBeanClass = field.asClassType().asClassElement();
        List<AnnotationAssist<ValidatorBuilder>> targetBuilderSpecList = getBuilderSpecs(targetBeanClass);
        AnnotationAssist<ValidatorBuilder> targetBuilderSpec = null;
        for (AnnotationAssist<ValidatorBuilder> tbs : targetBuilderSpecList) {
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

        ValidatorBuilderElement fieldValidatorBuilder = createValidatorBuilderElement(targetBeanClass, targetBuilderSpec);
        BeanElement validatedBean = new BeanElement(targetBeanClass.asClassType());
        validatedBean.addValidatorBuilder(fieldValidatorBuilder);

        String subject = validateBeanSpec.unwrap().subject();

        if (StringUtils.isEmpty(subject)) {
            subject = field.getName();
        }

        String command = validateBeanSpec.unwrap().command();

        BeanValidationElement beanValidation = new BeanValidationElement(field, subject, command, fieldValidatorBuilder);

        validatorBuilder.addValidation(beanValidation);
    }

    protected void parseFieldsValidations(ValidatorBuilderElement validatorBuilder) {
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

    protected ValidatorBuilderElement createValidatorBuilderElement(ClassElement beanClass, AnnotationAssist<ValidatorBuilder> builderSpec) {

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
            name = ValidatorBuilder.DEFAULT_BUILDER;
        }

        String command = builderSpec.unwrap().command();

        ValidatorBuilderElement validatorBuilder = new ValidatorBuilderElement(name, packageName,
                ClassType.of(processingEnv, superclass), command);

        return validatorBuilder;
    }

    protected void parseValidatedBean(BeanElement validatedBean, AnnotationAssist<ValidatorBuilder> builderSpec) {
        ValidatorBuilderElement validatorBuilder = createValidatorBuilderElement(validatedBean.getOriginType().asClassElement(), builderSpec);
        validatedBean.addValidatorBuilder(validatorBuilder);
        parseFieldsValidations(validatorBuilder);
    }

    protected List<AnnotationAssist<ValidatorBuilder>> getBuilderSpecs(ClassElement beanClass) {
        List<AnnotationAssist<ValidatorBuilder>> result = new ArrayList<>();
        AnnotationAssist<ValidatorBuilder> builderSpec = beanClass.getAnnotation(ValidatorBuilder.class);
        if (builderSpec != null) {
            result.add(builderSpec);
        } else {
            AnnotationAssist<ValidatorBuilders> buildersSpec = beanClass.getAnnotation(ValidatorBuilders.class);
            if (buildersSpec != null) {
                ValidatorBuilder[] builderSpecArr = buildersSpec.unwrap().value();
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

    public BeanElement parse(ClassElement beanClass) {
        BeanElement validatedBean = new BeanElement(beanClass.asClassType());
        List<AnnotationAssist<ValidatorBuilder>> builderSpecList = getBuilderSpecs(beanClass);
        for (AnnotationAssist<ValidatorBuilder> builderSpec : builderSpecList) {
            parseValidatedBean(validatedBean, builderSpec);
        }
        return validatedBean;
    }
}
