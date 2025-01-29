package colesico.framework.translation.codegen.generator;

import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.resource.assist.codegen.ResourceL10nOptionsGenerator;
import colesico.framework.translation.codegen.model.DictionaryElement;
import com.palantir.javapoet.MethodSpec;

import javax.annotation.processing.ProcessingEnvironment;

public class ResourceL10nGenerator extends FrameworkAbstractGenerator {

    private static final String L10N_OPTIONS_CLASS_SUFFIX = "L10nOptions";


    public ResourceL10nGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(DictionaryElement dictionaryElement) {

        ResourceL10nOptionsGenerator optionsGenerator;

        String optionsClassSimpleName = dictionaryElement.getOriginBean().getSimpleName() + L10N_OPTIONS_CLASS_SUFFIX;
        String optionsPackage = dictionaryElement.getOriginBean().getPackageName();;

        logger.debug("Generate  resource L10n options: " + optionsPackage + "." + optionsClassSimpleName);
        optionsGenerator = new ResourceL10nOptionsGenerator(optionsPackage, optionsClassSimpleName, this.getClass(), processingEnv);

        MethodSpec.Builder mb = optionsGenerator.configureMethod();

        optionsGenerator.generate(dictionaryElement.getOriginBean().unwrap());

    }


}
