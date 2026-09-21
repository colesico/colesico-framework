package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.WriteOptions;

/**
 * Basic write options for interaction via http
 */
public interface HttpWriteOptions extends WriteOptions {

    /**
     * Default HTTP Status Code
     */
    Integer status();

    /**
     * Default content-type
     */
    ContentType contentType();

}
