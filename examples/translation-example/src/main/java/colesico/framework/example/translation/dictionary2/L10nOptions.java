package colesico.framework.example.translation.dictionary2;

import colesico.framework.config.Config;
import colesico.framework.example.translation.dictionary.AppDictionary;
import colesico.framework.resource.l10n.L10nOptionsPrototype;

@Config
public class L10nOptions extends L10nOptionsPrototype {

    /**
     * Configure es localization in separate package
     */
    @Override
    public void configure(Options options) {
        options.baseClass(AppDictionary.class, "dictionary", "dictionary2")
                .qualifiers().language("es");
    }

}
