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

package colesico.framework.weblet.teleapi.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.weblet.VariousResponse;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.WriterContext;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class VariousWriter implements WebletTeleWriter<VariousResponse> {

    private final NavigationWriter navigationWriter;
    private final StringWriter stringWriter;
    private final BinaryWriter binaryWriter;

    private final Provider<HttpResponse> responseProv;

    @Inject
    public VariousWriter(NavigationWriter navigationWriter,
                         StringWriter stringWriter,
                         BinaryWriter binaryWriter,
                         Provider<HttpResponse> responseProv) {
        this.navigationWriter = navigationWriter;
        this.stringWriter = stringWriter;
        this.binaryWriter = binaryWriter;
        this.responseProv = responseProv;
    }

    @Override
    public void write(VariousResponse value, WriterContext wrContext) {
        if (value.getNavigationResponse() != null) {
            navigationWriter.write(value.getNavigationResponse(), wrContext);
        } else if (value.getStringResponse() != null) {
            stringWriter.write(value.getStringResponse(), wrContext);
        } else if (value.getBinaryResponse() != null) {
            binaryWriter.write(value.getBinaryResponse(), wrContext);
        } else {
            throw new RuntimeException("Undefined weblet response");
        }
    }
}
