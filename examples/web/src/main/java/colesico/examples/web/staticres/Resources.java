package colesico.examples.web.staticres;

import colesico.framework.router.Route;
import colesico.framework.webstatic.StaticContent;
import colesico.framework.weblet.Weblet;

@Weblet
public class Resources {

    private final StaticContent staticResource;

    public Resources(StaticContent.Builder staticResourceBuilder) {
        this.staticResource = staticResourceBuilder
                .resourcesRoot(Resources.class.getPackageName()
                        .replace('.', '/')
                        + "/webpub")
                .build();
    }

    // http://localhost:8080/resources/wheel.png
    @Route("/*")
    public void get(String routeSuffix, String l10nMode) {
        staticResource.send(routeSuffix, l10nMode);
    }
}
