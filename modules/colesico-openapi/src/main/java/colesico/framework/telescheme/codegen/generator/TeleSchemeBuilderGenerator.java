package colesico.framework.telescheme.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleFacadeElement;
import colesico.framework.telescheme.TeleSchemeBuilder;
import colesico.framework.telescheme.codegen.model.TeleSchemeBuilderElement;
import com.squareup.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;

import static colesico.framework.teleapi.TeleFacade.TARGET_PROV_FIELD;
import static colesico.framework.teleapi.TeleFacade.TELE_DRIVER_FIELD;

public class TeleSchemeBuilderGenerator extends FrameworkAbstractGenerator {
    public TeleSchemeBuilderGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    protected void generateConstructor(TeleFacadeElement teleFacade, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addAnnotation(ClassName.get(Inject.class));
        mb.addModifiers(Modifier.PUBLIC);
        mb.addParameter(
                ParameterizedTypeName.get(ClassName.get(Provider.class), TypeName.get(teleFacade.getParentService().getOriginClass().getOriginType())),
                TARGET_PROV_FIELD,
                Modifier.FINAL);

        mb.addParameter(ClassName.get(teleFacade.getTeleDriverClass()), TELE_DRIVER_FIELD, Modifier.FINAL);
        mb.addStatement("super($N, $N)", TARGET_PROV_FIELD, TELE_DRIVER_FIELD);
        classBuilder.addMethod(mb.build());
    }

    protected void generateBuildMethod(TeleSchemeBuilderElement teleScheme, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(TeleSchemeBuilder.BUILD_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        mb.returns(ClassName.get(teleScheme.getSchemeType()));
        mb.addCode(teleScheme.getBuildCode());
        classBuilder.addMethod(mb.build());
    }

    protected void createTeleSchemeClass(TeleSchemeBuilderElement teleScheme, TypeSpec.Builder classBuilder) {
        final TypeSpec typeSpec = classBuilder.build();
        String packageName = teleScheme.getParentTeleFacade().getParentService().getOriginClass().getPackageName();
        ServiceElement service = teleScheme.getParentTeleFacade().getParentService();
        CodegenUtils.createJavaFile(getProcessingEnv(), typeSpec, packageName, service.getOriginClass().unwrap());
    }

    public void generate(TeleSchemeBuilderElement teleScheme) {

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(teleScheme.getFacadeSchemeClassSimpleName());
        classBuilder.addModifiers(Modifier.PUBLIC);
        classBuilder.addModifiers(Modifier.FINAL);

        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null,
                "Service: " + teleScheme.getParentTeleFacade().getParentService().getOriginClass().unwrap().getQualifiedName().toString());
        classBuilder.addAnnotation(genstamp);

        classBuilder.addAnnotation(ClassName.get(Singleton.class));

        if (teleScheme.getBuilderBaseClass() == null) {
            classBuilder.superclass(ParameterizedTypeName.get(
                    ClassName.get(TeleSchemeBuilder.class),
                    ClassName.get(teleScheme.getSchemeType())
            ));
        } else {
            classBuilder.superclass(ClassName.get(teleScheme.getBuilderBaseClass()));
        }

        // generateConstructor(teleFacade, classBuilder);

        generateBuildMethod(teleScheme, classBuilder);

        createTeleSchemeClass(teleScheme, classBuilder);
    }
}
