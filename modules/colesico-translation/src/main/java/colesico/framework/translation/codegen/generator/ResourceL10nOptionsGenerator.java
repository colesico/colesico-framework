package colesico.framework.translation.codegen.generator;

import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.resource.localization.L10nOptionsPrototype;
import colesico.framework.translation.TranslationExceprion;
import colesico.framework.translation.codegen.model.BundleElement;
import colesico.framework.translation.codegen.model.DictionaryElement;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class ResourceL10nOptionsGenerator extends FrameworkAbstractGenerator {

    private static final String L10N_OPTIONS_CLASS_SUFFIX = "L10nOptions";


    public ResourceL10nOptionsGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(DictionaryElement dictionaryElement) {

        colesico.framework.resource.assist.codegen.ResourceL10nOptionsGenerator optionsGenerator;

        String optionsClassSimpleName = dictionaryElement.getOriginBean().getSimpleName() + L10N_OPTIONS_CLASS_SUFFIX;
        String optionsPackage = dictionaryElement.getOriginBean().getPackageName();

        logger.debug("Generate  resource L10n options: " + optionsPackage + "." + optionsClassSimpleName);
        optionsGenerator = new colesico.framework.resource.assist.codegen.ResourceL10nOptionsGenerator(optionsPackage, optionsClassSimpleName, this.getClass(), processingEnv);

        MethodSpec.Builder mb = optionsGenerator.configureMethod();
        CodeBlock.Builder cb = CodeBlock.builder();

        // TODO: test
        String pathTemplate =   dictionaryElement.getBasePath() + "{Q}" + ".properties";

        cb.add("$N.$N($S)", L10nOptionsPrototype.OPTIONS_PARAM,
                L10nOptionsPrototype.Options.PATH_METHOD, pathTemplate
        );

        cb.indent();

        Set<String> languageTags = dictionaryElement.getBundlesByLocale().values()
                .stream().map(BundleElement::getLanguageTag).collect(Collectors.toSet());

        languageTags.addAll(dictionaryElement.getExtraTranslations());

        for (String languageTag : languageTags) {
            if (StringUtils.isEmpty(languageTag)) {
                continue;
            }
            cb.add("\n.$N()", L10nOptionsPrototype.Options.QUALIFIERS_METHOD);
            Locale locale = Locale.forLanguageTag(languageTag);
            boolean emptyQualifiers = true;
            if (StringUtils.isNotEmpty(locale.getLanguage())) {
                emptyQualifiers = false;
                cb.add(".$N($S)",
                        L10nOptionsPrototype.Options.LANGUAGE_METHOD,
                        locale.getLanguage()
                );
            }
            if (StringUtils.isNotEmpty(locale.getCountry())) {
                emptyQualifiers = false;
                cb.add("\n.$N($S)",
                        L10nOptionsPrototype.Options.COUNTRY_METHOD,
                        locale.getCountry()
                );
            }
            if (StringUtils.isNotEmpty(locale.getVariant())) {
                emptyQualifiers = false;
                cb.add("\n.$N($S)",
                        L10nOptionsPrototype.Options.VARIANT_METHOD,
                        locale.getVariant()
                );
            }

            if (emptyQualifiers) {
                throw new TranslationExceprion("Invalid language tag: " + languageTag);
            }
        }
        cb.add(";");

        mb.addCode(cb.build());
        optionsGenerator.generate(dictionaryElement.getOriginBean().unwrap());

    }


}
