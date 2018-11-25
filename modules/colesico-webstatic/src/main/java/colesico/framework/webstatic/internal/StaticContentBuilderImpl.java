package colesico.framework.webstatic.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.ioc.InjectionPoint;
import colesico.framework.ioc.Message;
import colesico.framework.ioc.Unscoped;
import colesico.framework.resource.ResourceKit;
import colesico.framework.webstatic.StaticContent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;

@Unscoped
public class StaticContentBuilderImpl implements StaticContent.Builder {

    protected Logger log = LoggerFactory.getLogger(StaticContent.Builder.class);
    protected final Provider<HttpContext> httpContextProv;
    protected final ResourceKit resourceKit;
    protected String resourcesRoot;

    public StaticContentBuilderImpl(
            @Message InjectionPoint injectionPoint,
            Provider<HttpContext> httpContextProv,
            ResourceKit resourceKit) {

        this.httpContextProv = httpContextProv;
        this.resourceKit = resourceKit;

        if (injectionPoint != null) {
            String moduleName = injectionPoint.getTargetClass().getModule().getName();
            if (moduleName!=null) {
                String contentRootPkg = moduleName.replace('.', '/') + "/" + DEFAULT_RESOURCES_DIR;
                this.resourcesRoot = contentRootPkg;
            } else {
                throw new RuntimeException("Unnamed module for injection point: "+injectionPoint);
            }
        }
        log.debug("Initial content root: " + resourcesRoot);
    }

    @Override
    public StaticContentBuilderImpl resourcesRoot(String path) {
        this.resourcesRoot = path;
        return this;
    }

    @Override
    public StaticContent build() {
        if (StringUtils.isBlank(resourcesRoot)) {
            throw new RuntimeException("Undefined resources root");
        }
        return new StaticContentImpl(httpContextProv, resourceKit, resourcesRoot);
    }
}
