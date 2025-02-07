package colesico.framework.beanvalidation.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.beanvalidation.BeanValidatorBuilder;
import colesico.framework.beanvalidation.codegen.model.*;
import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.builder.FieldReference;
import colesico.framework.dslvalidator.builder.ValidationFlowBuilder;
import com.palantir.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.util.List;


/**
 * Bean Validator Prototype Builder generator
 */
public class ValidatorBuilderGenerator extends FrameworkAbstractGenerator {


    protected TypeSpec.Builder classBuilder;
    protected ValidatorBuilderElement builderElement;

    public ValidatorBuilderGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    private void generateFieldReferences(ValidatorBuilderElement validatorBuilder) {

        for (ValidationElement validation : validatorBuilder.getValidations()) {

            TypeName refType = ParameterizedTypeName.get(
                    ClassName.get(FieldReference.class),
                    TypeName.get(validatorBuilder.getParentBean().getOriginType().unwrap()),
                    TypeName.get(validation.getPropertyType())
            );

            FieldSpec.Builder refField = FieldSpec.builder(refType, validation.getPropertyReferenceName(), Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
            refField.initializer("new $T($S,$T::$N)",
                    refType,
                    validation.getSubject(),
                    TypeName.get(validatorBuilder.getParentBean().getOriginType().unwrap()),
                    validation.getPropertyGetterName()
            );

            classBuilder.addField(refField.build());
        }
    }

    private void generateValidateBeanMethod(BeanValidationElement validation) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(validation.getValidationMethodName());
        mb.addModifiers(Modifier.PROTECTED);
        TypeName returnType = ParameterizedTypeName.get(ClassName.get(Command.class), TypeName.get(validation.getPropertyType()));
        mb.returns(returnType);
        mb.addJavadoc("Validate $N", validation.getPropertyName());

        // return mandatory(validatorBuilderField1.validation())
        mb.addStatement("return $N.$N()", validation.getValidatorBuilderFieldName(), BeanValidatorBuilder.VALIDATION_METHOD);
        classBuilder.addMethod(mb.build());
    }

    private void generateValidatePropertyMethod(PropertyValidationElement validation) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(validation.getValidationMethodName());
        mb.addModifiers(Modifier.ABSTRACT);
        mb.addModifiers(Modifier.PROTECTED);
        TypeName returnType = ParameterizedTypeName.get(ClassName.get(Command.class), TypeName.get(validation.getPropertyType()));
        mb.returns(returnType);
        mb.addJavadoc("Validate $N", validation.getPropertyName());
        classBuilder.addMethod(mb.build());
    }

    private void generateVerifyPropertyMethod(PropertyValidationElement validation) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(validation.getValidationMethodName());
        mb.addModifiers(Modifier.ABSTRACT, Modifier.PROTECTED);
        mb.returns(TypeName.VOID);
        TypeName paramType = ParameterizedTypeName.get(ClassName.get(ValidationContext.class), TypeName.get(validation.getPropertyType()));
        mb.addParameter(paramType, "ctx", Modifier.FINAL);
        mb.addJavadoc("Verify $N", validation.getPropertyName());
        classBuilder.addMethod(mb.build());
    }


    private void generatePropertyValidationMethods(ValidatorBuilderElement validatorBuilder) {
        for (ValidationElement validation : validatorBuilder.getValidations()) {
            if (validation instanceof PropertyValidationElement propertyValidation) {
                if (propertyValidation.getVerifier()) {
                    generateVerifyPropertyMethod(propertyValidation);
                } else {
                    generateValidatePropertyMethod(propertyValidation);
                }
            } else {
                generateValidateBeanMethod((BeanValidationElement) validation);
            }
        }
    }


    private void generateRootValidationMethod(ValidatorBuilderElement validatorBuilder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(BeanValidatorBuilder.VALIDATION_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);

        TypeName returnsTypeName = ParameterizedTypeName.get(
                ClassName.get(Command.class),
                TypeName.get(validatorBuilder.getParentBean().getOriginType().unwrap()));

        mb.returns(returnsTypeName);

        CodeBlock.Builder cb = CodeBlock.builder();

        cb.add("return $N(\n", validatorBuilder.getCommand());
        cb.indent();
        int i = 0;
        for (ValidationElement validation : validatorBuilder.getValidations()) {
            // field(FIELD_REF,
            cb.add("$N($N, ", ValidationFlowBuilder.MAP_METHOD, validation.getPropertyReferenceName());
            if (validation instanceof PropertyValidationElement propertyValidation) {
                if (propertyValidation.getVerifier()) {
                    // this::verifyField1
                    cb.add("this::$N", validation.getValidationMethodName());
                } else {
                    // validateField1()
                    cb.add("$N()", validation.getValidationMethodName());
                }
            } else {
                cb.add("$N()", validation.getValidationMethodName());
            }
            cb.add(")");
            if (++i < validatorBuilder.getValidations().size()) {
                cb.add(",");
            }
            cb.add("\n");
        }
        cb.unindent();
        cb.add(");\n");

        mb.addCode(cb.build());
        classBuilder.addMethod(mb.build());
    }


    private void generateProxyConstructors(ValidatorBuilderElement builderElement) {

        List<MethodElement> constructors = builderElement.getSuperclass().asClassElement().getConstructorsFiltered(
                c -> c.unwrap().getModifiers().contains(Modifier.PUBLIC)
        );

        for (MethodElement constructor : constructors) {
            MethodSpec.Builder constructorBuilder = CodegenUtils.createProxyMethodBuilder(
                    constructor, null, null, false
            );
            CodeBlock sucall = CodegenUtils.generateSuperMethodCall(constructor, null, null);
            constructorBuilder.addCode(sucall);

            // Generate extra params
            for (ValidationElement validation : builderElement.getValidations()) {
                if (validation instanceof BeanValidationElement beanValidation) {
                    TypeName builderType = ClassName.bestGuess(beanValidation.getFieldValidatorBuilder().getBuilderClassName());
                    String builderVarName = beanValidation.getValidatorBuilderFieldName();
                    constructorBuilder.addParameter(builderType, builderVarName, Modifier.FINAL);
                    constructorBuilder.addStatement("this.$N = $N", builderVarName, builderVarName);
                }
            }

            classBuilder.addMethod(constructorBuilder.build());
        }
    }

    private void generateBuildersFields() {
        for (ValidationElement validations : builderElement.getValidations()) {
            if (validations instanceof BeanValidationElement beanValidation) {
                TypeName builderType = ClassName.bestGuess(beanValidation.getFieldValidatorBuilder().getBuilderClassName());
                String builderVarName = beanValidation.getValidatorBuilderFieldName();
                FieldSpec.Builder fb = FieldSpec.builder(builderType, builderVarName, Modifier.PROTECTED, Modifier.FINAL);
                fb.addJavadoc(" Validator Builder for " + validations.getPropertyName());
                classBuilder.addField(fb.build());
            }
        }
    }

    public void generate(BeanElement validatedBean) {
        for (ValidatorBuilderElement validatorBuilder : validatedBean.getValidatorBuilders()) {

            this.builderElement = validatorBuilder;

            this.classBuilder = TypeSpec.classBuilder(validatorBuilder.getBuilderClassSimpleName());
            classBuilder.addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);

            TypeName superClassName = ParameterizedTypeName.get(
                    (ClassName) ClassName.get(validatorBuilder.getSuperclass().unwrap()),
                    TypeName.get(validatedBean.getOriginType().unwrap()));
            classBuilder.superclass(superClassName);

            classBuilder.addJavadoc("Validator Builder Prototype\nExtend this class to  implement validation methods");

            generateFieldReferences(validatorBuilder);
            generateBuildersFields();
            generateProxyConstructors(validatorBuilder);
            generatePropertyValidationMethods(validatorBuilder);
            generateRootValidationMethod(validatorBuilder);

            CodegenUtils.createJavaFile(processingEnv, classBuilder.build(), validatorBuilder.getPackageName(), validatorBuilder.getParentBean().getOriginType().asTypeElement());
        }
    }
}
