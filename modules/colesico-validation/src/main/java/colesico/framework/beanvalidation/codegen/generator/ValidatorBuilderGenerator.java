package colesico.framework.beanvalidation.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.beanvalidation.BeanValidatorBuilder;
import colesico.framework.beanvalidation.codegen.model.ValidateAsBeanElement;
import colesico.framework.beanvalidation.codegen.model.ValidatedBeanElement;
import colesico.framework.beanvalidation.codegen.model.ValidatedPropertyElement;
import colesico.framework.beanvalidation.codegen.model.ValidatorBuilderElement;
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

        for (ValidatedPropertyElement validatedProperty : validatorBuilder.getProperties()) {

            TypeName refType = ParameterizedTypeName.get(
                    ClassName.get(FieldReference.class),
                    TypeName.get(validatorBuilder.getParentBean().getOriginType().unwrap()),
                    TypeName.get(validatedProperty.getPropertyType())
            );

            FieldSpec.Builder refField = FieldSpec.builder(refType, validatedProperty.getPropertyReferenceName(), Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
            refField.initializer("new $T($S,$T::$N)",
                    refType,
                    validatedProperty.getSubject(),
                    TypeName.get(validatorBuilder.getParentBean().getOriginType().unwrap()), validatedProperty.getPropertyGetterName()
            );

            classBuilder.addField(refField.build());
        }
    }

    private void generateValidateMethod(ValidatedPropertyElement propertyElm) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(propertyElm.getMethodName());

        if (propertyElm.getValidateWithBuilder() == null) {
            mb.addModifiers(Modifier.ABSTRACT);
        }

        mb.addModifiers(Modifier.PROTECTED);
        TypeName returnType = ParameterizedTypeName.get(ClassName.get(Command.class), TypeName.get(propertyElm.getPropertyType()));
        mb.returns(returnType);
        mb.addJavadoc("Validate $N", propertyElm.getPropertyName());

        if (propertyElm.getValidateWithBuilder() != null) {
            ValidateAsBeanElement vab = propertyElm.getValidateWithBuilder();

            String groupCmd = vab.isOptional() ? ValidationFlowBuilder.OPTIONAL_GROUP_METHOD : ValidationFlowBuilder.MANDATORY_GROUP_METHOD;

            // return optional(validatorBuilderField1.commands())
            // return mandatory(validatorBuilderField1.commands())
            mb.addStatement("return $N($N.$N())", groupCmd, vab.validatorFieldName(), BeanValidatorBuilder.COMMANDS_METHOD);
        }

        classBuilder.addMethod(mb.build());
    }

    private void generateVerifyMethod(ValidatedPropertyElement validatedProperty) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(validatedProperty.getMethodName());
        mb.addModifiers(Modifier.ABSTRACT, Modifier.PROTECTED);
        mb.returns(TypeName.VOID);
        TypeName paramType = ParameterizedTypeName.get(ClassName.get(ValidationContext.class), TypeName.get(validatedProperty.getPropertyType()));
        mb.addParameter(paramType, "ctx", Modifier.FINAL);
        mb.addJavadoc("Verify $N", validatedProperty.getPropertyName());
        classBuilder.addMethod(mb.build());
    }


    private void generatePropertyValidationMethods(ValidatorBuilderElement validatorBuilder) {
        for (ValidatedPropertyElement propertyElm : validatorBuilder.getProperties()) {
            if (propertyElm.getVerifier()) {
                generateVerifyMethod(propertyElm);
            } else {
                generateValidateMethod(propertyElm);
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

        mb.returns(ArrayTypeName.of(returnsTypeName));

        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("return $N(){\n", returnsTypeName);
        cb.indent();
        int i = 0;
        for (ValidatedPropertyElement propertyElement : validatorBuilder.getProperties()) {
            // field("name1", v->v.getField1(),
            cb.add("$N( $S, v->v.$N(), ", ValidationFlowBuilder.FIELD_METHOD, propertyElement.getSubject(), propertyElement.getPropertyGetterName());
            if (propertyElement.getVerifier()) {
                // this::verifyField1
                cb.add("this::$N", propertyElement.getMethodName());
            } else {
                // validateField1()
                cb.add("$N()", propertyElement.getMethodName());
            }
            cb.add(")");
            if (++i < validatorBuilder.getProperties().size()) {
                cb.add(",");
            }
            cb.add("\n");

        }
        cb.unindent();
        cb.add("};\n");

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
            for (ValidatedPropertyElement vpe : builderElement.getProperties()) {
                if (vpe.getValidateWithBuilder() == null) {
                    continue;
                }
                TypeName builderType = ClassName.bestGuess(vpe.getValidateWithBuilder().getBuilderClass());
                String builderVarName = vpe.getValidateWithBuilder().validatorFieldName();
                constructorBuilder.addParameter(builderType, builderVarName, Modifier.FINAL);
                constructorBuilder.addStatement("this.$N = $N", builderVarName, builderVarName);
            }

            classBuilder.addMethod(constructorBuilder.build());
        }
    }

    private void generateValidatorBuildersFields() {
        for (ValidatedPropertyElement vpe : builderElement.getProperties()) {
            if (vpe.getValidateWithBuilder() == null) {
                continue;
            }
            TypeName builderType = ClassName.bestGuess(vpe.getValidateWithBuilder().getBuilderClass());
            String builderVarName = vpe.getValidateWithBuilder().validatorFieldName();
            FieldSpec.Builder fb = FieldSpec.builder(builderType, builderVarName, Modifier.PROTECTED, Modifier.FINAL);
            fb.addJavadoc(" Validator Builder for " + vpe.getPropertyName());
            classBuilder.addField(fb.build());
        }
    }

    public void generate(ValidatedBeanElement validatedBean) {
        for (ValidatorBuilderElement validatorBuilder : validatedBean.getBuilders()) {

            this.builderElement = validatorBuilder;

            this.classBuilder = TypeSpec.classBuilder(validatorBuilder.getClassSimpleName());
            classBuilder.addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);

            TypeName superClassName = ParameterizedTypeName.get(
                    (ClassName) ClassName.get(validatorBuilder.getSuperclass().unwrap()),
                    TypeName.get(validatedBean.getOriginType().unwrap()));
            classBuilder.superclass(superClassName);

            classBuilder.addJavadoc("Validator Builder Prototype\nExtend this class to  implement validation methods");

            generateFieldReferences(validatorBuilder);
            generateValidatorBuildersFields();
            generateProxyConstructors(validatorBuilder);
            generatePropertyValidationMethods(validatorBuilder);
            generateRootValidationMethod(validatorBuilder);

            CodegenUtils.createJavaFile(processingEnv, classBuilder.build(), validatorBuilder.getPackageName(), validatorBuilder.getParentBean().getOriginType().asTypeElement());
        }
    }
}
