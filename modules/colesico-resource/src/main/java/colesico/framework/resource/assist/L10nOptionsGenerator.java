package colesico.framework.resource.assist;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.config.Config;
import colesico.framework.resource.l10n.L10nOptionsPrototype;
import com.palantir.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class L10nOptionsGenerator extends FrameworkAbstractGenerator {


    protected final Class<?> masterGeneratorClass;

    protected final String optionsPackageName;
    protected final String optionsClassSimpleName;
    protected final String optionsClassName;
    protected final String optionsClassFilePath;

    protected MethodSpec.Builder configureMethodBuilder = null;
    protected CodeBlock.Builder configureCodeBlockBuilder = null;

    public L10nOptionsGenerator(String optionsPackageName, String optionsClassSimpleName, Class<?> masterGeneratorClass, ProcessingEnvironment processingEnv) {
        super(processingEnv);

        logger.debug("Initializing resource l10n options generator: " + optionsPackageName + "." + optionsClassSimpleName);

        this.masterGeneratorClass = masterGeneratorClass;
        this.optionsPackageName = optionsPackageName;
        this.optionsClassSimpleName = optionsClassSimpleName;

        this.optionsClassName = this.optionsPackageName + '.' + this.optionsClassSimpleName;
        this.optionsClassFilePath = "/" + StringUtils.replace(optionsClassName, ".", "/") + ".java";
    }


    public CodeBlock.Builder configureMethod() {
        if (configureCodeBlockBuilder != null) {
            return configureCodeBlockBuilder;
        }
        configureMethodBuilder = MethodSpec.methodBuilder(L10nOptionsPrototype.CONFIGURE_METHOD);
        configureMethodBuilder.addModifiers(Modifier.PUBLIC);
        configureMethodBuilder.addAnnotation(Override.class);
        configureMethodBuilder.addParameter(L10nOptionsPrototype.Options.class, L10nOptionsPrototype.OPTIONS_PARAM, Modifier.FINAL);
        configureMethodBuilder.returns(TypeName.VOID);

        configureCodeBlockBuilder = CodeBlock.builder();

        return configureCodeBlockBuilder;
    }

    public TypeSpec.Builder typeBuilder() {
        TypeSpec.Builder optionsBuilder = TypeSpec.classBuilder(optionsClassSimpleName);
        optionsBuilder.superclass(L10nOptionsPrototype.class);
        optionsBuilder.addModifiers(Modifier.PUBLIC);
        optionsBuilder.addModifiers(Modifier.FINAL);
        optionsBuilder.addAnnotation(CodegenUtils.generateGenstamp(masterGeneratorClass.getName(), null, null));
        optionsBuilder.addAnnotation(Config.class);

        return optionsBuilder;
    }

    public L10nOptionsGenerator l10nOptions() {
        configureCodeBlockBuilder.add("$N", L10nOptionsPrototype.OPTIONS_PARAM);
        return this;
    }

    public L10nOptionsGenerator baseName(String baseName) {
        configureCodeBlockBuilder.add(".$N($S)", L10nOptionsPrototype.Options.BASE_NAME_METHOD, baseName);
        return this;
    }

    public L10nOptionsGenerator baseClass(Class baseClass) {
        configureCodeBlockBuilder.add(".$N($T.class)", L10nOptionsPrototype.Options.BASE_CLASS_METHOD,
                ClassName.get(baseClass));
        return this;
    }

    public L10nOptionsGenerator qualifiers() {
        configureCodeBlockBuilder.add("\n.$N()", L10nOptionsPrototype.Options.QUALIFIERS_METHOD);
        return this;
    }

    public L10nOptionsGenerator language(String lang) {
        configureCodeBlockBuilder.add(".$N($S)",
                L10nOptionsPrototype.Options.LANGUAGE_METHOD,
                lang
        );
        return this;
    }

    public L10nOptionsGenerator country(String country) {
        configureCodeBlockBuilder.add(".$N($S)",
                L10nOptionsPrototype.Options.COUNTRY_METHOD,
                country
        );
        return this;
    }

    public L10nOptionsGenerator variant(String variant) {
        configureCodeBlockBuilder.add(".$N($S)",
                L10nOptionsPrototype.Options.VARIANT_METHOD,
                variant
        );
        return this;
    }

    public void generate(Element... originatingElements) {
        TypeSpec.Builder tb = typeBuilder();
        configureMethodBuilder.addCode(configureCodeBlockBuilder.build());
        tb.addMethod(configureMethodBuilder.build());
        final TypeSpec typeSpec = tb.build();
        CodegenUtils.createJavaFile(processingEnv, typeSpec, optionsPackageName, originatingElements);
    }


}
