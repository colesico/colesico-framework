package colesico.framework.translation.codegen.generator;

import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.translation.TranslationExceprion;
import colesico.framework.translation.codegen.model.BundleElement;
import colesico.framework.translation.codegen.model.DictionaryElement;
import com.palantir.javapoet.CodeBlock;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class L10nOptionsGenerator extends FrameworkAbstractGenerator {

    private static final String L10N_OPTIONS_CLASS_SUFFIX = "L10nOptions";


    public L10nOptionsGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(DictionaryElement dictionaryElement) {

        colesico.framework.resource.assist.L10nOptionsGenerator optionsGenerator;

        String optionsClassSimpleName = dictionaryElement.getOriginBean().simpleName() + L10N_OPTIONS_CLASS_SUFFIX;
        String optionsPackage = dictionaryElement.getOriginBean().packageName();

        logger.debug("Generate  resource L10n options: " + optionsPackage + "." + optionsClassSimpleName);
        optionsGenerator = new colesico.framework.resource.assist.L10nOptionsGenerator(optionsPackage, optionsClassSimpleName, this.getClass(), processingEnv);

        CodeBlock.Builder cb = optionsGenerator.configureMethod();

        String resourceNameTemplate = dictionaryElement.getBaseName() + "{Q}";

        optionsGenerator.options().baseName(resourceNameTemplate);
        cb.indent();

        Set<String> languageTags = dictionaryElement.getBundlesByLocale().values()
                .stream().map(BundleElement::getLanguageTag).collect(Collectors.toSet());

        languageTags.addAll(dictionaryElement.getExtraTranslations());

        for (String languageTag : languageTags) {
            if (StringUtils.isEmpty(languageTag)) {
                continue;
            }
            optionsGenerator.qualifiers();
            Locale locale = Locale.forLanguageTag(languageTag);
            boolean emptyQualifiers = true;
            if (StringUtils.isNotEmpty(locale.getLanguage())) {
                emptyQualifiers = false;
                optionsGenerator.language(locale.getLanguage());
            }
            if (StringUtils.isNotEmpty(locale.getCountry())) {
                emptyQualifiers = false;
                optionsGenerator.country(locale.getCountry());
            }
            if (StringUtils.isNotEmpty(locale.getVariant())) {
                emptyQualifiers = false;
                optionsGenerator.variant(locale.getVariant());
            }

            if (emptyQualifiers) {
                throw new TranslationExceprion("Invalid language tag: " + languageTag);
            }
        }

        cb.add(";");
        optionsGenerator.generate(dictionaryElement.getOriginBean().unwrap());

    }


}
