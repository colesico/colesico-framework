package colesico.framework.service.codegen.model.teleapi;

/**
 * Element that value can be read from data port
 */
public interface TeleReadableElement {

    ReadOptionsElement readOptions();

    void setReadOptions(ReadOptionsElement readOptions);
}
