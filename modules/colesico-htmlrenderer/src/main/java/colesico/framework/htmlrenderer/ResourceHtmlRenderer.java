package colesico.framework.htmlrenderer;

import colesico.framework.assist.StrUtils;
import colesico.framework.weblet.HtmlResponse;
import org.apache.commons.lang3.StringUtils;

abstract public class ResourceHtmlRenderer implements HtmlRenderer<String> {

    protected final String templatesRootPath;

    public ResourceHtmlRenderer(String templatesRootPath) {
        while (StringUtils.startsWith(templatesRootPath, "/")) {
            templatesRootPath = StringUtils.substring(templatesRootPath, 1);
        }
        this.templatesRootPath = templatesRootPath;
    }

    abstract protected <M> String doRender(String templateFullPath, M model);

    @Override
    public final <M> HtmlResponse render(String templatePath, M model) {
        String templateFullPath;
        if (StringUtils.startsWith(templatePath, "/")) {
            templateFullPath = templatePath;
        } else if (StringUtils.startsWith(templatePath, ".")) {
            templateFullPath = StrUtils.concatPath(templatesRootPath, templatePath.substring(1), "/");
        } else {
            templateFullPath = StrUtils.concatPath(templatesRootPath, templatePath, "/");
        }

        String html = doRender(templateFullPath, model);
        return new HtmlResponse(html);
    }

}
