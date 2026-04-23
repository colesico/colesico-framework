package colesico.framework.service.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.teleapi.TeleFacadeElement;
import colesico.framework.service.codegen.model.teleapi.TeleSchemeElement;
import colesico.framework.teleapi.TeleScheme;
import com.palantir.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import javax.lang.model.element.Modifier;

import static colesico.framework.teleapi.TeleFacade.TARGET_PROV_FIELD;

public class TeleSchemeGenerator extends FrameworkAbstractGenerator {
    public TeleSchemeGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected void generateConstructor(TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addAnnotation(ClassName.get(Inject.class));
        mb.addModifiers(Modifier.PUBLIC);
        mb.addParameter(
                ParameterizedTypeName.get(ClassName.get(Provider.class), TypeName.get(teleFacade.parentService().originClass().getOriginType())),
                TARGET_PROV_FIELD,
                Modifier.FINAL);
        /*
        mb.addParameter(ClassName.get(teleFacade.getTeleControllerClass()), TELE_DRIVER_FIELD, Modifier.FINAL);
        mb.addStatement("super($N, $N)", TARGET_PROV_FIELD, TELE_DRIVER_FIELD);
        */
        classBuilder.addMethod(mb.build());

    }

    protected void generateBuildMethod(TeleSchemeElement schemeBuilder, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(TeleScheme.BUILD_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        mb.returns(ClassName.get(schemeBuilder.schemeType()));
        mb.addCode(schemeBuilder.buildMethodBody());
        classBuilder.addMethod(mb.build());
    }

    protected void createSchemeBuilderClassFile(TeleSchemeElement schemeBuilder, TypeSpec.Builder classBuilder) {
        final TypeSpec typeSpec = classBuilder.build();
        String packageName = schemeBuilder.parentTeleFacade().parentService().originClass().getPackageName();
        ServiceElement service = schemeBuilder.parentTeleFacade().parentService();
        CodegenUtils.createJavaFile(getProcessingEnv(), typeSpec, packageName, service.originClass().unwrap());
    }

    public void generate(TeleSchemeElement schemeBuilder) {

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(schemeBuilder.teleSchemeClassSimpleName());
        classBuilder.addModifiers(Modifier.PUBLIC);
        classBuilder.addModifiers(Modifier.FINAL);

        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null,
                "Service: " + schemeBuilder.parentTeleFacade().parentService().originClass().unwrap().getQualifiedName().toString());
        classBuilder.addAnnotation(genstamp);

        classBuilder.addAnnotation(ClassName.get(Singleton.class));

        if (schemeBuilder.baseClass() == null) {
            classBuilder.addSuperinterface(ParameterizedTypeName.get(
                    ClassName.get(TeleScheme.class),
                    ClassName.get(schemeBuilder.schemeType())
            ));
        } else {
            classBuilder.superclass(ClassName.get(schemeBuilder.baseClass()));
        }

        // generateConstructor(teleFacade, classBuilder);

        generateBuildMethod(schemeBuilder, classBuilder);

        createSchemeBuilderClassFile(schemeBuilder, classBuilder);
    }
}
