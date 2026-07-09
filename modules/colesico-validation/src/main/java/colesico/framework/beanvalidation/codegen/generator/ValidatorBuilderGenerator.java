package colesico.framework.beanvalidation.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.beanvalidation.BeanValidatorBuilder;
import colesico.framework.beanvalidation.codegen.model.*;
import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.builder.FieldReference;
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

    private void generateFieldReferences(ValidatorBuilderElement validationBuilder) {

        for (ValidateElement validation : validationBuilder.validations()) {

            TypeName refType = ParameterizedTypeName.get(
                    ClassName.get(FieldReference.class),
                    TypeName.get(validationBuilder.parentBean().originType().unwrap()),
                    TypeName.get(validation.fieldType())
            );

            FieldSpec.Builder refField = FieldSpec.builder(refType, validation.filedReferenceName(), Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
            refField.initializer("new $T($S,$T::$N)",
                    refType,
                    validation.subject(),
                    TypeName.get(validationBuilder.parentBean().originType().unwrap()),
                    validation.fieldGetterName()
            );

            classBuilder.addField(refField.build());
        }
    }

    private void generateValidateBeanMethod(BeanValidateElement validation) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(validation.validationMethodName());
        mb.addModifiers(Modifier.PROTECTED);
        TypeName returnType = ParameterizedTypeName.get(ClassName.get(Command.class), TypeName.get(validation.fieldType()));
        mb.returns(returnType);
        mb.addJavadoc("Validate $N", validation.fieldName());

        // return mandatory(builderPrototypeField1.validation())
        mb.addStatement("return $N.$N()", validation.validatorBuilderFieldName(), BeanValidatorBuilder.VALIDATION_METHOD);
        classBuilder.addMethod(mb.build());
    }


    private void generatePropertyValidationMethods(ValidatorBuilderElement validatorBuilder) {
        for (ValidateElement validation : validatorBuilder.validations()) {
            if (validation instanceof BeanValidateElement bve) {
                generateValidateBeanMethod(bve);
            }
        }
    }

    private void generateSubjectMethod(ValidatorBuilderElement validatorBuilder) {
        if (validatorBuilder.subject() != null) {
            MethodSpec.Builder mb = MethodSpec.methodBuilder(BeanValidatorBuilder.SUBJECT_METHOD);
            mb.addAnnotation(Override.class);
            mb.returns(ClassName.get(String.class));
            mb.addModifiers(Modifier.PUBLIC);
            mb.addStatement("return $S", validatorBuilder.subject());
            classBuilder.addMethod(mb.build());
        }
    }

    private void generateRootValidationMethod(ValidatorBuilderElement validatorBuilder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(BeanValidatorBuilder.VALIDATION_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);

        TypeName returnsTypeName = ParameterizedTypeName.get(
                ClassName.get(Command.class),
                TypeName.get(validatorBuilder.parentBean().originType().unwrap()));

        mb.returns(returnsTypeName);

        CodeBlock.Builder cb = CodeBlock.builder();

        cb.add("return $N(\n", validatorBuilder.command());
        cb.indent();
        int i = 0;
        for (ValidateElement validation : validatorBuilder.validations()) {
            // map(FIELD_REF,
            cb.add("$N($N, ", validation.mapper(), validation.filedReferenceName());
            if (validation instanceof ValueValidateElement propertyValidation) {
                if (propertyValidation.verifier()) {
                    // this::verifyField1
                    cb.add("this::$N", validation.validationMethodName());
                } else {
                    // validateField1()
                    cb.add("$N()", validation.validationMethodName());
                }
            } else {
                cb.add("$N()", validation.validationMethodName());
            }
            cb.add(")");
            if (++i < validatorBuilder.validations().size()) {
                cb.add(",");
            }
            cb.add("\n");
        }
        cb.unindent();
        cb.add(");\n");

        mb.addCode(cb.build());
        classBuilder.addMethod(mb.build());
    }


    private void generateProxyConstructors(ValidatorBuilderElement validatorBuilder) {

        List<MethodElement> constructors = validatorBuilder.superclass().asClassElement().constructorsFiltered(
                c -> c.unwrap().getModifiers().contains(Modifier.PUBLIC)
        );

        for (MethodElement constructor : constructors) {
            MethodSpec.Builder constructorBuilder = CodegenUtils.createProxyMethodBuilder(
                    constructor, null, null, false
            );
            CodeBlock suCall = CodegenUtils.generateSuperMethodCall(constructor, null, null);
            constructorBuilder.addCode(suCall);

            // Generate extra params
            for (ValidateElement validation : validatorBuilder.validations()) {
                if (validation instanceof BeanValidateElement beanValidation) {
                    TypeName builderType = ClassName.bestGuess(beanValidation.fieldValidatorBuilder().builderClassName());
                    String builderVarName = beanValidation.validatorBuilderFieldName();
                    constructorBuilder.addParameter(builderType, builderVarName, Modifier.FINAL);
                    constructorBuilder.addStatement("this.$N = $N", builderVarName, builderVarName);
                }
            }

            classBuilder.addMethod(constructorBuilder.build());
        }
    }

    private void generateBuildersFields() {
        for (ValidateElement validations : builderElement.validations()) {
            if (validations instanceof BeanValidateElement beanValidation) {
                TypeName builderType = ClassName.bestGuess(beanValidation.fieldValidatorBuilder().builderClassName());
                String builderVarName = beanValidation.validatorBuilderFieldName();
                FieldSpec.Builder fb = FieldSpec.builder(builderType, builderVarName, Modifier.PROTECTED, Modifier.FINAL);
                fb.addJavadoc(" Validator Builder for " + validations.fieldName());
                classBuilder.addField(fb.build());
            }
        }
    }

    public void generate(BeanElement validatedBean) {
        for (ValidatorBuilderElement validatorBuilder : validatedBean.validatorBuilders()) {

            this.builderElement = validatorBuilder;

            this.classBuilder = TypeSpec.classBuilder(validatorBuilder.builderClassSimpleName());
            classBuilder.addModifiers(Modifier.PUBLIC);

            TypeName superClassName = ClassName.get(validatorBuilder.superclass().unwrap());
            classBuilder.superclass(superClassName);

            classBuilder.addJavadoc("Validator Builder");

            generateFieldReferences(validatorBuilder);
            generateBuildersFields();
            generateProxyConstructors(validatorBuilder);
            generatePropertyValidationMethods(validatorBuilder);
            generateSubjectMethod(validatorBuilder);
            generateRootValidationMethod(validatorBuilder);

            CodegenUtils.createJavaFile(processingEnv, classBuilder.build(), validatorBuilder.packageName(), validatorBuilder.parentBean().originType().asTypeElement());
        }
    }
}
