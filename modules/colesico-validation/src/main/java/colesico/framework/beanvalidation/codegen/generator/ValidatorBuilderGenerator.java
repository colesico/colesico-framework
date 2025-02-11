package colesico.framework.beanvalidation.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.beanvalidation.BeanValidatorBuilder;
import colesico.framework.beanvalidation.codegen.model.*;
import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.builder.FieldReference;
import com.palantir.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.util.List;


/**
 * Bean Validator Prototype Builder generator
 */
public class ValidatorBuilderGenerator extends FrameworkAbstractGenerator {


    protected TypeSpec.Builder classBuilder;
    protected BuilderPrototypeElement builderElement;

    public ValidatorBuilderGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    private void generateFieldReferences(BuilderPrototypeElement validatorBuilder) {

        for (ValidateElement validation : validatorBuilder.getValidations()) {

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

    private void generateValidateBeanMethod(BeanValidateElement validation) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(validation.getValidationMethodName());
        mb.addModifiers(Modifier.PROTECTED);
        TypeName returnType = ParameterizedTypeName.get(ClassName.get(Command.class), TypeName.get(validation.getPropertyType()));
        mb.returns(returnType);
        mb.addJavadoc("Validate $N", validation.getPropertyName());

        // return mandatory(validatorBuilderField1.validation())
        mb.addStatement("return $N.$N()", validation.getValidatorBuilderFieldName(), BeanValidatorBuilder.VALIDATION_METHOD);
        classBuilder.addMethod(mb.build());
    }

    private void generateValidatePropertyMethod(PropertyValidateElement validation) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(validation.getValidationMethodName());
        mb.addModifiers(Modifier.ABSTRACT);
        mb.addModifiers(Modifier.PROTECTED);
        TypeName returnType = ParameterizedTypeName.get(ClassName.get(Command.class), TypeName.get(validation.getPropertyType()));
        mb.returns(returnType);
        mb.addJavadoc("Validate $N", validation.getPropertyName());
        classBuilder.addMethod(mb.build());
    }

    private void generateVerifyPropertyMethod(PropertyValidateElement validation) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(validation.getValidationMethodName());
        mb.addModifiers(Modifier.ABSTRACT, Modifier.PROTECTED);
        mb.returns(TypeName.VOID);
        TypeName paramType = ParameterizedTypeName.get(ClassName.get(ValidationContext.class), TypeName.get(validation.getPropertyType()));
        mb.addParameter(paramType, "ctx", Modifier.FINAL);
        mb.addJavadoc("Verify $N", validation.getPropertyName());
        classBuilder.addMethod(mb.build());
    }


    private void generatePropertyValidationMethods(BuilderPrototypeElement validatorBuilder) {
        for (ValidateElement validation : validatorBuilder.getValidations()) {
            if (validation instanceof PropertyValidateElement propertyValidation) {
                if (propertyValidation.getVerifier()) {
                    generateVerifyPropertyMethod(propertyValidation);
                } else {
                    generateValidatePropertyMethod(propertyValidation);
                }
            } else {
                generateValidateBeanMethod((BeanValidateElement) validation);
            }
        }
    }

    private void generateSubjectMethod(BuilderPrototypeElement validatorBuilder) {
        if (StringUtils.isBlank(validatorBuilder.getSubject())) {
            MethodSpec.Builder mb = MethodSpec.methodBuilder(BeanValidatorBuilder.SUBJECT_METHOD);
            mb.addAnnotation(Override.class);
            mb.returns(ClassName.get(String.class));
            mb.addModifiers(Modifier.PUBLIC);
            mb.addStatement("return $S", validatorBuilder.getSubject());
            classBuilder.addMethod(mb.build());
        }
    }

    private void generateRootValidationMethod(BuilderPrototypeElement validatorBuilder) {
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
        for (ValidateElement validation : validatorBuilder.getValidations()) {
            // map(FIELD_REF,
            cb.add("$N($N, ", validation.getMapper(), validation.getPropertyReferenceName());
            if (validation instanceof PropertyValidateElement propertyValidation) {
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


    private void generateProxyConstructors(BuilderPrototypeElement validatorBuilder) {

        List<MethodElement> constructors = validatorBuilder.getSuperclass().asClassElement().getConstructorsFiltered(
                c -> c.unwrap().getModifiers().contains(Modifier.PUBLIC)
        );

        for (MethodElement constructor : constructors) {
            MethodSpec.Builder constructorBuilder = CodegenUtils.createProxyMethodBuilder(
                    constructor, null, null, false
            );
            CodeBlock suCall = CodegenUtils.generateSuperMethodCall(constructor, null, null);
            constructorBuilder.addCode(suCall);

            // Generate extra params
            for (ValidateElement validation : validatorBuilder.getValidations()) {
                if (validation instanceof BeanValidateElement beanValidation) {
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
        for (ValidateElement validations : builderElement.getValidations()) {
            if (validations instanceof BeanValidateElement beanValidation) {
                TypeName builderType = ClassName.bestGuess(beanValidation.getFieldValidatorBuilder().getBuilderClassName());
                String builderVarName = beanValidation.getValidatorBuilderFieldName();
                FieldSpec.Builder fb = FieldSpec.builder(builderType, builderVarName, Modifier.PROTECTED, Modifier.FINAL);
                fb.addJavadoc(" Validator Builder for " + validations.getPropertyName());
                classBuilder.addField(fb.build());
            }
        }
    }

    public void generate(BeanElement validatedBean) {
        for (BuilderPrototypeElement validatorBuilder : validatedBean.getValidatorBuilders()) {

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
            generateSubjectMethod(validatorBuilder);
            generateRootValidationMethod(validatorBuilder);

            CodegenUtils.createJavaFile(processingEnv, classBuilder.build(), validatorBuilder.getPackageName(), validatorBuilder.getParentBean().getOriginType().asTypeElement());
        }
    }
}
