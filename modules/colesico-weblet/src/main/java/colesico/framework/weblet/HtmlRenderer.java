package colesico.framework.weblet;

/**
 * Default html  template engine
 */
public interface HtmlRenderer {
    String render(String templateName, Object model);
}
