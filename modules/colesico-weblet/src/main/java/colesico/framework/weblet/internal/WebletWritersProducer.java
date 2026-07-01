/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.weblet.internal;

import colesico.framework.ioc.production.Classed;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.telehttp.TeleHttpWriter;
import colesico.framework.weblet.response.FileResponse;
import colesico.framework.weblet.writer.FileResponseWriter;
import jakarta.inject.Singleton;

@Producer
@Produce(FileResponseWriter.class)
public class WebletWritersProducer {

    @Singleton
    @Classed(FileResponse.class)
    public TeleHttpWriter binaryResponseWriter(FileResponseWriter impl) {
        return impl;
    }

}
