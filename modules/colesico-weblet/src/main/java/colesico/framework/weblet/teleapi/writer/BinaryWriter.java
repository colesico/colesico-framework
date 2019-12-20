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

package colesico.framework.weblet.teleapi.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.weblet.BinaryResponse;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.WriterContext;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.nio.ByteBuffer;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class BinaryWriter implements WebletTeleWriter<BinaryResponse> {

    private final Provider<HttpResponse> responseProv;

    @Inject
    public BinaryWriter(Provider<HttpResponse> responseProv) {
        this.responseProv = responseProv;
    }

    @Override
    public void write(BinaryResponse value, WriterContext wrContext) {
        ByteBuffer buffer = ByteBuffer.wrap(value.getContent());
        HttpResponse response = responseProv.get();
        //force download?
        if (value.getFileName() != null) {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + value.getFileName() + "\"");
        }
        response.sendData(buffer, value.getContentType(), 200);
    }
}
