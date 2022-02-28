package colesico.framework.service.codegen.model.teleapi;

/**
 * Element that value can be read from data port
 */
public interface TeleInputElement {

    TRContextElement getReadingContext();

    void setReadingContext(TRContextElement readingContext);
}
