package colesico.framework.resource.assist.codegen;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.config.Config;
import colesico.framework.resource.ResourceL10nOptionsPrototype;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class ResourceL10nOptionsGenerator extends FrameworkAbstractGenerator {


    protected final Class<?> masterGeneratorClass;

    protected final String optionsPackageName;
    protected final String optionsClassSimpleName;
    protected final String optionsClassName;
    protected final String optionsClassFilePath;

    protected MethodSpec.Builder configureMethodBuilder = null;

    public ResourceL10nOptionsGenerator(String optionsPackageName, String optionsClassSimpleName, Class<?> masterGeneratorClass, ProcessingEnvironment processingEnv) {
        super(processingEnv);

        logger.debug("Initializing resource l10n options generator: " + optionsPackageName + "." + optionsClassSimpleName);

        this.masterGeneratorClass = masterGeneratorClass;
        this.optionsPackageName = optionsPackageName;
        this.optionsClassSimpleName = optionsClassSimpleName;

        this.optionsClassName = this.optionsPackageName + '.' + this.optionsClassSimpleName;
        this.optionsClassFilePath = "/" + StringUtils.replace(optionsClassName, ".", "/") + ".java";
    }


    public MethodSpec.Builder configureMethod() {
        if (configureMethodBuilder != null) {
            return configureMethodBuilder;
        }
        MethodSpec.Builder configureMethodBuilder = MethodSpec.methodBuilder(ResourceL10nOptionsPrototype.CONFIGURE_METHOD);
        configureMethodBuilder.addModifiers(Modifier.PUBLIC);
        configureMethodBuilder.addAnnotation(Override.class);
        configureMethodBuilder.addParameter(ResourceL10nOptionsPrototype.Options.class, ResourceL10nOptionsPrototype.OPTIONS_PARAM, Modifier.FINAL);
        configureMethodBuilder.returns(TypeName.VOID);
        return configureMethodBuilder;
    }

    public TypeSpec.Builder typeBuilder() {
        TypeSpec.Builder optionsBuilder = TypeSpec.classBuilder(optionsClassSimpleName);
        optionsBuilder.addModifiers(Modifier.PUBLIC);
        optionsBuilder.addModifiers(Modifier.FINAL);
        optionsBuilder.addAnnotation(CodegenUtils.generateGenstamp(masterGeneratorClass.getName(), null, null));
        optionsBuilder.addAnnotation(Config.class);

        optionsBuilder.addMethod(configureMethodBuilder.build());

        return optionsBuilder;
    }

    public void generate(Element... originatingElements) {
        TypeSpec.Builder tb = typeBuilder();
        final TypeSpec typeSpec = tb.build();
        CodegenUtils.createJavaFile(processingEnv, typeSpec, optionsPackageName, originatingElements);
    }


}
