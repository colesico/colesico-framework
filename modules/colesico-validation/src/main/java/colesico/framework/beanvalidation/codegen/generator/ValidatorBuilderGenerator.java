package colesico.framework.beanvalidation.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.beanvalidation.codegen.model.ValidatedBeanElement;
import colesico.framework.beanvalidation.codegen.model.ValidatedPropertyElement;
import colesico.framework.beanvalidation.codegen.model.ValidatorBuilderElement;
import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.DSLValidator;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.builder.FlowControlBuilder;
import com.squareup.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.util.List;


/**
 * Validator builder (builder prototype) generator
 */
public class ValidatorBuilderGenerator extends FrameworkAbstractGenerator {


    protected TypeSpec.Builder classBuilder;
    protected ValidatorBuilderElement vbpElement;

    public ValidatorBuilderGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }


    private void generateValidateMethod(ValidatedPropertyElement propertyElm) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(propertyElm.getMethodName());
        mb.addModifiers(Modifier.ABSTRACT, Modifier.PROTECTED);
        TypeName returnType = ParameterizedTypeName.get(ClassName.get(Command.class), TypeName.get(propertyElm.getPropertyType()));
        mb.returns(returnType);
        mb.addJavadoc("Validate $N", propertyElm.getPropertyName());
        classBuilder.addMethod(mb.build());
    }

    private void generateVerifyMethod(ValidatedPropertyElement propertyElm) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(propertyElm.getMethodName());
        mb.addModifiers(Modifier.ABSTRACT, Modifier.PROTECTED);
        mb.returns(TypeName.VOID);
        TypeName paramType = ParameterizedTypeName.get(ClassName.get(ValidationContext.class), TypeName.get(propertyElm.getPropertyType()));
        mb.addParameter(paramType, "ctx", Modifier.FINAL);
        mb.addJavadoc("Verify $N", propertyElm.getPropertyName());
        classBuilder.addMethod(mb.build());
    }

    private void generatePropertyValidationMethods(ValidatorBuilderElement builderElement) {
        for (ValidatedPropertyElement propertyElement : builderElement.getProperties()) {
            if (propertyElement.getVerify()) {
                generateVerifyMethod(propertyElement);
            } else {
                generateValidateMethod(propertyElement);
            }
        }
    }

    private void generateCommandsMethod(ValidatorBuilderElement builderElement) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(builderElement.getCommandsMethodName());
        mb.addModifiers(Modifier.PUBLIC);
        mb.returns(ArrayTypeName.of(ClassName.get(Command.class)));

        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("return $T.<$T>$N(\n", ClassName.get(FlowControlBuilder.class),
                TypeName.get(builderElement.getParentBean().getOriginType().unwrap()),
                FlowControlBuilder.COMMANDS_METHOD
        );
        cb.indent();
        int i = 0;
        for (ValidatedPropertyElement propertyElement : builderElement.getProperties()) {
            // field("name1", v->v.getField1(),
            cb.add("$N( $S, v->v.$N(), ", FlowControlBuilder.FIELD_METHOD, propertyElement.getSubject(), propertyElement.getPropertyGetterName());
            if (propertyElement.getVerify()) {
                // this::verifyField1
                cb.add("this::$N", propertyElement.getMethodName());
            } else {
                // validateField1()
                cb.add("$N()", propertyElement.getMethodName());
            }
            cb.add(")");
            if (++i < builderElement.getProperties().size()) {
                cb.add(",");
            }
            cb.add("\n");

        }
        cb.unindent();
        cb.add(");\n");

        mb.addCode(cb.build());
        classBuilder.addMethod(mb.build());
    }

    private void generateBuildMethod(ValidatorBuilderElement builderElement) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(builderElement.getBuildMethodName());
        mb.addModifiers(Modifier.PUBLIC);
        TypeName returnType = ParameterizedTypeName.get(ClassName.get(DSLValidator.class), TypeName.get(builderElement.getParentBean().getOriginType().unwrap()));
        mb.returns(returnType);

        mb.addStatement("return $N($N())", FlowControlBuilder.PROGRAM_METHOD, builderElement.getCommandsMethodName());

        classBuilder.addMethod(mb.build());
    }

    private void generateProxyConstructors(ValidatorBuilderElement builderElement) {

        List<MethodElement> constructors = builderElement.getExtendsClass().asClassElement().getConstructorsFiltered(
                c -> c.unwrap().getModifiers().contains(Modifier.PUBLIC)
        );

        for (MethodElement constructor : constructors) {
            MethodSpec.Builder constructorBuilder = CodegenUtils.createProxyMethodBuilder(
                    constructor, null, null, false
            );
            CodeBlock sucall = CodegenUtils.generateSuperMethodCall(constructor, null, null);
            constructorBuilder.addCode(sucall);
            classBuilder.addMethod(constructorBuilder.build());
        }
    }

    public void generate(ValidatedBeanElement beanElement) {
        for (ValidatorBuilderElement builderElement : beanElement.getBuilders()) {

            this.vbpElement = builderElement;

            this.classBuilder = TypeSpec.classBuilder(builderElement.getTargetClassName());

            classBuilder.addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);

            classBuilder.superclass(TypeName.get(builderElement.getExtendsClass().unwrap()));

            generateProxyConstructors(builderElement);
            generatePropertyValidationMethods(builderElement);
            generateCommandsMethod(builderElement);
            generateBuildMethod(builderElement);

            CodegenUtils.createJavaFile(processingEnv, classBuilder.build(), builderElement.getTargetPackageName(), builderElement.getParentBean().getOriginType().asTypeElement());

        }
    }
}
