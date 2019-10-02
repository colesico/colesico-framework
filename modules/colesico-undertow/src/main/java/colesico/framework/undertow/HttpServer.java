/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */
package colesico.framework.undertow;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.form.EagerFormParsingHandler;
import io.undertow.server.handlers.form.FormEncodedDataDefinition;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.server.handlers.form.MultiPartParserDefinition;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Main HTTP server
 *
 * @author Vladlen Larionov
 */
@Singleton
public class HttpServer {

    public static final String ENCODING = "UTF-8";

    protected Undertow server;
    protected Undertow.Builder builder;

    protected final UndertowConfig config;

    protected final RouterHandler routerHandler;

    @Inject
    public HttpServer(RouterHandler routerHandler, UndertowConfig config) {
        this.config = config;
        this.routerHandler = routerHandler;
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

    public HttpServer init() {
        try {
            builder = Undertow.builder();
            config.applyOptions(builder);
            HttpHandler formHandler = makeFormHandler(routerHandler);
            builder.setHandler(config.getRootHandler(formHandler));
            server = builder.build();
            return this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public synchronized void start() {
        server.start();
    }

    public synchronized void stop() {
        server.stop();
    }

}
