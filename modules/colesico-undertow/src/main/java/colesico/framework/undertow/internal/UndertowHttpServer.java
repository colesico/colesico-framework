/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package colesico.framework.undertow.internal;

import colesico.framework.httpserver.HttpServer;
import colesico.framework.undertow.UndertowConfigPrototype;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.form.EagerFormParsingHandler;
import io.undertow.server.handlers.form.FormEncodedDataDefinition;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Undertow  based HTTP server
 *
 * @author Vladlen Larionov
 */
@Singleton
public class UndertowHttpServer implements HttpServer {

    protected final Logger logger = LoggerFactory.getLogger(UndertowHttpServer.class);

    public static final String ENCODING = "UTF-8";

    protected Undertow server;

    protected final UndertowConfigPrototype config;

    protected final UndertowHttpHandler requestProcessor;

    @Inject
    public UndertowHttpServer(UndertowConfigPrototype config, UndertowHttpHandler requestProcessor) {
        this.config = config;
        this.requestProcessor = requestProcessor;
    }

    protected HttpHandler makeFormHandler(HttpHandler nextHandler) {
        FormParserFactory.Builder builder = FormParserFactory.builder(false);
        FormEncodedDataDefinition form = new FormEncodedDataDefinition();
        form.setDefaultEncoding(ENCODING);
        MultiPartParserDefinition mult = new MultiPartParserDefinition();
        mult.setDefaultEncoding(ENCODING);
        mult.setMaxIndividualFileSize(config.getMaxIndividualFileSize());
        builder.addParsers(form, mult);
        EagerFormParsingHandler efp = new EagerFormParsingHandler(builder.build());
        return efp.setNext(nextHandler);
    }

    protected Undertow build() {
        try {
            Undertow.Builder builder = Undertow.builder();
            HttpHandler rootHandler = makeFormHandler(requestProcessor);
            if (config.enableStoredResponses()) {
                rootHandler = new StoreResponseConduit.StoreResponseHandler(rootHandler);
            }
            builder.setHandler(config.getRootHandler(rootHandler));
            config.applyOptions(builder);
            server = builder.build();
            return server;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Undertow getServer() {
        return server;
    }

    public synchronized HttpServer start() {
        build().start();
        return this;
    }

    public synchronized void stop() {
        server.stop();
    }

}
