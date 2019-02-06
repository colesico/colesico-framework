package colesico.framework.example.web.localization;

import colesico.framework.profile.ProfileKit;
import colesico.framework.profile.DefaultProfile;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

import java.util.Locale;

@Weblet
public class MyLocalization {

    private final ProfileKit l10n;
    private final MyDictionary translations;

    public MyLocalization(ProfileKit l10n, MyDictionary translations) {
        this.l10n = l10n;
        this.translations = translations;
    }

    // http://localhost:8080/my-localization/ru
    public HtmlResponse ru() {
        l10n.setProfile(new DefaultProfile(new Locale("ru", "RU")));
        return new HtmlResponse("Русский");
    }

    // http://localhost:8080/my-localization/en
    public HtmlResponse en() {
        l10n.setProfile(new DefaultProfile(new Locale("en", "GB")));
        return new HtmlResponse("English");
    }

    // http://localhost:8080/my-localization/message
    public HtmlResponse message() {
        return new HtmlResponse(translations.hello1());
    }

    // http://localhost:8080/my-localization/inherit
    public HtmlResponse inherit() {
        l10n.setProfile(new DefaultProfile(new Locale("ru", "RU")));
        return new HtmlResponse(translations.hello3());
    }
}
