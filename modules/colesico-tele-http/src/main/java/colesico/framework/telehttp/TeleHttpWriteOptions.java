package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.WriteOptions;

/**
 * Basic write options for interaction via http
 */
public interface TeleHttpWriteOptions<W extends TeleHttpWriter<?, ?>> extends WriteOptions<W> {

    /**
     * Default HTTP Status Code
     */
    Integer statusCode();

    /**
     * Default media-type
     */
    MediaType mediaType();

}
