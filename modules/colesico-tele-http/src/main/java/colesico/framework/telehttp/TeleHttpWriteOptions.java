package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.WriteOptions;

import java.nio.charset.Charset;

/**
 * Basic write options for interaction via http
 */
public interface TeleHttpWriteOptions extends WriteOptions {

    /**
     * Default HTTP Status Code
     */
    Integer statusCode();

    /**
     * Default content type
     */
    String contentType();

    /**
     * Default charset
     */
    Charset charset();

    /**
     *  Overrides the default writer to be used for reading the value
     */
    Class<? extends TeleHttpWriter<?, ?>> writerClass();
}
