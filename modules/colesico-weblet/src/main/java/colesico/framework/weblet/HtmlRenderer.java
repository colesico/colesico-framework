package colesico.framework.weblet;

/**
 * Default html renderer template engine
 */
public interface HtmlRenderer {
    String renderer(String templateName, Object model);
}
