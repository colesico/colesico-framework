package colesico.framework.weblet;

/**
 * Default html  template engine
 */
public interface HtmlRenderer {
    
    String render(String templateName, Object model);

    default String render(String templateName) {
        return render(templateName, null);
    }
}
