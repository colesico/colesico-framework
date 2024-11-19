package colesico.framework.service.codegen.generator;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.teleapi.TeleFacadeElement;
import colesico.framework.teleapi.TeleScheme;
import colesico.framework.service.codegen.model.teleapi.TeleSchemeElement;
import com.palantir.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;

import static colesico.framework.teleapi.TeleFacade.TARGET_PROV_FIELD;
import static colesico.framework.teleapi.TeleFacade.TELE_DRIVER_FIELD;

public class TeleSchemeGenerator extends FrameworkAbstractGenerator {
    public TeleSchemeGenerator(ProcessingEnvironment processingEnv) {
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

    protected void generateBuildMethod(TeleSchemeElement schemeBuilder, TypeSpec.Builder classBuilder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(TeleScheme.BUILD_METHOD);
        mb.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        mb.returns(ClassName.get(schemeBuilder.getSchemeType()));
        mb.addCode(schemeBuilder.getBuildMethodBody());
        classBuilder.addMethod(mb.build());
    }

    protected void createSchemeBuilderClassFile(TeleSchemeElement schemeBuilder, TypeSpec.Builder classBuilder) {
        final TypeSpec typeSpec = classBuilder.build();
        String packageName = schemeBuilder.getParentTeleFacade().getParentService().getOriginClass().getPackageName();
        ServiceElement service = schemeBuilder.getParentTeleFacade().getParentService();
        CodegenUtils.createJavaFile(getProcessingEnv(), typeSpec, packageName, service.getOriginClass().unwrap());
    }

    public void generate(TeleSchemeElement schemeBuilder) {

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(schemeBuilder.getTeleSchemeClassSimpleName());
        classBuilder.addModifiers(Modifier.PUBLIC);
        classBuilder.addModifiers(Modifier.FINAL);

        AnnotationSpec genstamp = CodegenUtils.generateGenstamp(this.getClass().getName(), null,
                "Service: " + schemeBuilder.getParentTeleFacade().getParentService().getOriginClass().unwrap().getQualifiedName().toString());
        classBuilder.addAnnotation(genstamp);

        classBuilder.addAnnotation(ClassName.get(Singleton.class));

        if (schemeBuilder.getBaseClass() == null) {
            classBuilder.addSuperinterface(ParameterizedTypeName.get(
                    ClassName.get(TeleScheme.class),
                    ClassName.get(schemeBuilder.getSchemeType())
            ));
        } else {
            classBuilder.superclass(ClassName.get(schemeBuilder.getBaseClass()));
        }

        // generateConstructor(teleFacade, classBuilder);

        generateBuildMethod(schemeBuilder, classBuilder);

        createSchemeBuilderClassFile(schemeBuilder, classBuilder);
    }
}
