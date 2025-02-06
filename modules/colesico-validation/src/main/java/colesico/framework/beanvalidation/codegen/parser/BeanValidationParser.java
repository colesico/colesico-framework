package colesico.framework.beanvalidation.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.beanvalidation.*;
import colesico.framework.beanvalidation.codegen.model.ValidateAsBeanElement;
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

    public static final String VALIDATOR_BUILDER_PROTOTYPE_SUFFIX = "Validation";

    public BeanValidationParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected void parseValidatableFields(ValidatorBuilderElement validatorBuilder) {
        logger.debug("Parse validatable bean fields : " + validatorBuilder);

        ClassElement beanClass = validatorBuilder.getParentBean().getOriginType().asClassElement();
        List<FieldElement> fieldsList = beanClass.getFieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.STATIC)
        );

        for (FieldElement fieldElm : fieldsList) {
            logger.debug("Process validatable bean field: {} of type {}", fieldElm.getName(), fieldElm.unwrap().asType());
            AnnotationAssist<Validate> validateSpec = fieldElm.getAnnotation(Validate.class);
            AnnotationAssist<ValidateAsBean> validateAsBeanSpec = fieldElm.getAnnotation(ValidateAsBean.class);
            if ((validateSpec == null) && (validateAsBeanSpec == null)) {
                continue;
            }

            String subject = validateSpec == null ? null : validateSpec.unwrap().subject();

            if (StringUtils.isEmpty(subject)) {
                subject = fieldElm.getName();
            }

            String methodName = validateSpec == null ? null : validateSpec.unwrap().methodName();
            if (StringUtils.isEmpty(methodName)) {
                methodName = null;
            }

            boolean verifier = validateSpec == null ? false : validateSpec.unwrap().verifier();

            ValidateAsBeanElement validateBean = null;
            if (validateAsBeanSpec != null) {
                String builderClassName = validateAsBeanSpec.unwrap().builderClass();
                if (StringUtils.isEmpty(builderClassName)) {
                    ClassElement fieldClass = fieldElm.asClassType().asClassElement();
                    List<AnnotationAssist<ValidatorBuilder>> builderSpecList = getBuilderSpecs(fieldClass);
                    ValidatorBuilderElement fieldValidatorBuilder = createValidatorBuilderElement(fieldClass, builderSpecList.get(0));
                    builderClassName = fieldValidatorBuilder.getPackageName() + '.' + fieldValidatorBuilder.getClassSimpleName();
                }
                validateBean = new ValidateAsBeanElement(builderClassName, validateAsBeanSpec.unwrap().optional());
            }

            ValidatedPropertyElement propertyElm = new ValidatedPropertyElement(fieldElm, subject, methodName, verifier, validateBean);
            validatorBuilder.addProperty(propertyElm);
        }
    }

    protected ValidatorBuilderElement createValidatorBuilderElement(ClassElement beanClass, AnnotationAssist<ValidatorBuilder> builderSpec) {
        DeclaredType extendsClass = (DeclaredType) builderSpec.getValueTypeMirror(a -> a.extendsClass());
        DeclaredType packageFromClass = (DeclaredType) builderSpec.getValueTypeMirror(a -> a.packageClass());

        String packageName = builderSpec.unwrap().packageName();
        if (StringUtils.isBlank(packageName)) {
            if (!CodegenUtils.isAssignable(Class.class, packageFromClass, processingEnv)) {
                packageName = (new ClassType(processingEnv, packageFromClass)).asClassElement().getPackageName();
            } else if (!CodegenUtils.isAssignable(BeanValidatorBuilder.class, extendsClass, processingEnv)) {
                packageName = (new ClassType(processingEnv, extendsClass)).asClassElement().getPackageName();
            } else {
                packageName = beanClass.getPackageName();
            }
        }

        String className = builderSpec.unwrap().classSimpleName();
        if (StringUtils.isBlank(className)) {
            className = beanClass.getSimpleName() + VALIDATOR_BUILDER_PROTOTYPE_SUFFIX;
        }

        ValidatorBuilderElement validatorBuilder = new ValidatorBuilderElement(
                packageName,
                className,
                new ClassType(processingEnv, extendsClass)
        );

        return validatorBuilder;
    }

    protected void parseBean(ValidatedBeanElement beanElement, AnnotationAssist<ValidatorBuilder> builderSpec) {
        ValidatorBuilderElement validatorBuilder = createValidatorBuilderElement(beanElement.getOriginType().asClassElement(), builderSpec);
        beanElement.addBuilder(validatorBuilder);
        parseValidatableFields(validatorBuilder);
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

    public ValidatedBeanElement parse(ClassElement beanClass) {
        ValidatedBeanElement validatedBean = new ValidatedBeanElement(beanClass.asClassType());
        List<AnnotationAssist<ValidatorBuilder>> builderSpecList = getBuilderSpecs(beanClass);
        for (AnnotationAssist<ValidatorBuilder> builderSpec : builderSpecList) {
            parseBean(validatedBean, builderSpec);
        }
        return validatedBean;
    }
}
