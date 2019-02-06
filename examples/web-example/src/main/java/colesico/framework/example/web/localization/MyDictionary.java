package colesico.framework.example.web.localization;

import colesico.framework.translation.Dictionary;
import colesico.framework.translation.TranslationKey;

@Dictionary(basePath = MyDictionary.BUNDLE_PATH)
public interface MyDictionary {

    String BUNDLE_PATH="colesico/framework/example/web/localization/t9n/Strings";

    @TranslationKey("Hello1")
    String hello1();

    String hello2();

    String hello3();
}
