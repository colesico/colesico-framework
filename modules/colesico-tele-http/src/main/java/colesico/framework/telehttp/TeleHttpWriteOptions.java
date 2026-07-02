package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.WriteOptions;

/**
 * Basic write options for interaction via http
 */
public interface TeleHttpWriteOptions extends WriteOptions {

    /**
     * Default HTTP Status Code
     */
    Integer statusCode();

    /**
     * Default content-type
     */
    ContentType contentType();

}
