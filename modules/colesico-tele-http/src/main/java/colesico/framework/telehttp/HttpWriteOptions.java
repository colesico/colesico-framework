package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.WriteOptions;

import java.nio.charset.Charset;

/**
 * Basic write options for interaction via http
 */
public interface HttpWriteOptions extends WriteOptions {

    /**
     * HTTP Status Code
     */
    Integer statusCode();

    String contentType();

    Charset charset();

}
