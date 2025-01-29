package colesico.framework.translation.codegen.generator;

import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.resource.ResourceL10nOptionsPrototype;
import colesico.framework.translation.codegen.model.BundleElement;
import colesico.framework.translation.codegen.model.DictionaryElement;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.Locale;

public class ResourceL10nOptionsGenerator extends FrameworkAbstractGenerator {

    private static final String L10N_OPTIONS_CLASS_SUFFIX = "L10nOptions";


    public ResourceL10nOptionsGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(DictionaryElement dictionaryElement) {

        colesico.framework.resource.assist.codegen.ResourceL10nOptionsGenerator optionsGenerator;

        String optionsClassSimpleName = dictionaryElement.getOriginBean().getSimpleName() + L10N_OPTIONS_CLASS_SUFFIX;
        String optionsPackage = dictionaryElement.getOriginBean().getPackageName();
        ;

        logger.debug("Generate  resource L10n options: " + optionsPackage + "." + optionsClassSimpleName);
        optionsGenerator = new colesico.framework.resource.assist.codegen.ResourceL10nOptionsGenerator(optionsPackage, optionsClassSimpleName, this.getClass(), processingEnv);

        MethodSpec.Builder mb = optionsGenerator.configureMethod();
        CodeBlock.Builder cb = CodeBlock.builder();

        String pathTemplate = "!!!"+ dictionaryElement.getBasePath() + "{Q}" + ".properties";

        cb.add("$N.$N($S)", ResourceL10nOptionsPrototype.OPTIONS_PARAM,
                ResourceL10nOptionsPrototype.Options.PATH_METHOD, pathTemplate
        );

        cb.indent();
        for (BundleElement bundleElem : dictionaryElement.getBundlesByLocale().values()) {
            if (StringUtils.isEmpty(bundleElem.getLocaleTag())) {
                continue;
            }
            cb.add("\n.$N()", ResourceL10nOptionsPrototype.Options.QUALIFIERS_METHOD);
            Locale locale = Locale.forLanguageTag(bundleElem.getLocaleTag());
            if (StringUtils.isNotEmpty(locale.getLanguage())) {
                cb.add(".$N($S)",
                        ResourceL10nOptionsPrototype.Options.LANGUAGE_METHOD,
                        locale.getLanguage()
                );
            }
            if (StringUtils.isNotEmpty(locale.getCountry())) {
                cb.add("\n.$N($S)",
                        ResourceL10nOptionsPrototype.Options.COUNTRY_METHOD,
                        locale.getCountry()
                );
            }
            if (StringUtils.isNotEmpty(locale.getVariant())) {
                cb.add("\n.$N($S)",
                        ResourceL10nOptionsPrototype.Options.VARIANT_METHOD,
                        locale.getVariant()
                );
            }
        }
        cb.add(";");

        mb.addCode(cb.build());
        optionsGenerator.generate(dictionaryElement.getOriginBean().unwrap());

    }


}
