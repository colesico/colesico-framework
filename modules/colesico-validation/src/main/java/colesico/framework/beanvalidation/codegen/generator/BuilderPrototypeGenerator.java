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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.util.List;


/**
 * Bean Validator Prototype Builder generator
 */
public class BuilderPrototypeGenerator extends FrameworkAbstractGenerator {


    protected TypeSpec.Builder classBuilder;
    protected BuilderPrototypeElement builderElement;

    public BuilderPrototypeGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    private void generateFieldReferences(BuilderPrototypeElement builderPrototype) {

        for (ValidateElement validation : builderPrototype.validations()) {

            TypeName refType = ParameterizedTypeName.get(
                    ClassName.get(FieldReference.class),
                    TypeName.get(builderPrototype.parentBean().originType().unwrap()),
                    TypeName.get(validation.propertyType())
            );

            FieldSpec.Builder refField = FieldSpec.builder(refType, validation.propertyReferenceName(), Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
            refField.initializer("new $T($S,$T::$N)",
                    refType,
                    validation.subject(),
                    TypeName.get(builderPrototype.parentBean().originType().unwrap()),
                    validation.propertyGetterName()
            );

            classBuilder.addField(refField.build());
        }
    }

    private void generateValidateBeanMethod(BeanValidateElement validation) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(validation.validationMethodName());
        mb.addModifiers(Modifier.PROTECTED);
        TypeName returnType = ParameterizedTypeName.get(ClassName.get(Command.class), TypeName.get(validation.propertyType()));
        mb.returns(returnType);
        mb.addJavadoc("Validate $N", validation.propertyName());

        // return mandatory(builderPrototypeField1.validation())
        mb.addStatement("return $N.$N()", validation.validatorBuilderFieldName(), BeanValidatorBuilder.VALIDATION_METHOD);
        classBuilder.addMethod(mb.build());
    }

    private void generateValidatePropertyMethod(PropertyValidateElement validation) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(validation.validationMethodName());
        mb.addModifiers(Modifier.ABSTRACT);
        mb.addModifiers(Modifier.PROTECTED);
        TypeName returnType = ParameterizedTypeName.get(ClassName.get(Command.class), TypeName.get(validation.propertyType()));
        mb.returns(returnType);
        mb.addJavadoc("Validate $N", validation.propertyName());
        classBuilder.addMethod(mb.build());
    }

    private void generateVerifyPropertyMethod(PropertyValidateElement validation) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(validation.validationMethodName());
        mb.addModifiers(Modifier.ABSTRACT, Modifier.PROTECTED);
        mb.returns(TypeName.VOID);
        TypeName paramType = ParameterizedTypeName.get(ClassName.get(ValidationContext.class), TypeName.get(validation.propertyType()));
        mb.addParameter(paramType, "ctx", Modifier.FINAL);
        mb.addJavadoc("Verify $N", validation.propertyName());
        classBuilder.addMethod(mb.build());
    }


    private void generatePropertyValidationMethods(BuilderPrototypeElement builderPrototype) {
        for (ValidateElement validation : builderPrototype.validations()) {
            if (validation instanceof PropertyValidateElement propertyValidation) {
                if (propertyValidation.verifier()) {
                    generateVerifyPropertyMethod(propertyValidation);
                } else {
                    generateValidatePropertyMethod(propertyValidation);
                }
            } else {
                generateValidateBeanMethod((BeanValidateElement) validation);
            }
        }
    }

    private void generateSubjectMethod(BuilderPrototypeElement builderPrototype) {
        if (builderPrototype.subject() != null) {
            MethodSpec.Builder mb = MethodSpec.methodBuilder(BeanValidatorBuilder.SUBJECT_METHOD);
            mb.addAnnotation(Override.class);
            mb.returns(ClassName.get(String.class));
            mb.addModifiers(Modifier.PUBLIC);
            mb.addStatement("return $S", builderPrototype.subject());
            classBuilder.addMethod(mb.build());
        }
    }

    private void generateRootValidationMethod(BuilderPrototypeElement builderPrototype) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(BeanValidatorBuilder.VALIDATION_METHOD);
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);

        TypeName returnsTypeName = ParameterizedTypeName.get(
                ClassName.get(Command.class),
                TypeName.get(builderPrototype.parentBean().originType().unwrap()));

        mb.returns(returnsTypeName);

        CodeBlock.Builder cb = CodeBlock.builder();

        cb.add("return $N(\n", builderPrototype.command());
        cb.indent();
        int i = 0;
        for (ValidateElement validation : builderPrototype.validations()) {
            // map(FIELD_REF,
            cb.add("$N($N, ", validation.mapper(), validation.propertyReferenceName());
            if (validation instanceof PropertyValidateElement propertyValidation) {
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
            if (++i < builderPrototype.validations().size()) {
                cb.add(",");
            }
            cb.add("\n");
        }
        cb.unindent();
        cb.add(");\n");

        mb.addCode(cb.build());
        classBuilder.addMethod(mb.build());
    }


    private void generateProxyConstructors(BuilderPrototypeElement builderPrototype) {

        List<MethodElement> constructors = builderPrototype.superclass().asClassElement().constructorsFiltered(
                c -> c.unwrap().getModifiers().contains(Modifier.PUBLIC)
        );

        for (MethodElement constructor : constructors) {
            MethodSpec.Builder constructorBuilder = CodegenUtils.createProxyMethodBuilder(
                    constructor, null, null, false
            );
            CodeBlock suCall = CodegenUtils.generateSuperMethodCall(constructor, null, null);
            constructorBuilder.addCode(suCall);

            // Generate extra params
            for (ValidateElement validation : builderPrototype.validations()) {
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
                fb.addJavadoc(" Validator Builder for " + validations.propertyName());
                classBuilder.addField(fb.build());
            }
        }
    }

    public void generate(BeanElement validatedBean) {
        for (BuilderPrototypeElement builderPrototype : validatedBean.validatorBuilders()) {

            this.builderElement = builderPrototype;

            this.classBuilder = TypeSpec.classBuilder(builderPrototype.builderClassSimpleName());
            classBuilder.addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);

            TypeName superClassName = ParameterizedTypeName.get(
                    (ClassName) ClassName.get(builderPrototype.superclass().unwrap()),
                    TypeName.get(validatedBean.originType().unwrap()));
            classBuilder.superclass(superClassName);

            classBuilder.addJavadoc("Validator Builder Prototype\nExtend this class to  implement validation methods");

            generateFieldReferences(builderPrototype);
            generateBuildersFields();
            generateProxyConstructors(builderPrototype);
            generatePropertyValidationMethods(builderPrototype);
            generateSubjectMethod(builderPrototype);
            generateRootValidationMethod(builderPrototype);

            CodegenUtils.createJavaFile(processingEnv, classBuilder.build(), builderPrototype.packageName(), builderPrototype.parentBean().originType().asTypeElement());
        }
    }
}
