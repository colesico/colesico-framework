package colesico.framework.service.codegen.model.teleapi;

/**
 * Element that value can be read from data port
 */
public interface TeleReadableElement {

    TROptionsElement readOptions();

    void setReadOptions(TROptionsElement readOptions);
}
