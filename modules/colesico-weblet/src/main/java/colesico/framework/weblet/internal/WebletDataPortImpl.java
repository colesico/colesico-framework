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

package colesico.framework.weblet.internal;

import colesico.framework.ioc.ClassedKey;
import colesico.framework.ioc.Ioc;
import colesico.framework.weblet.teleapi.*;

import javax.inject.Singleton;

@Singleton
public class WebletDataPortImpl implements WebletDataPort {

    protected final Ioc ioc;

    public WebletDataPortImpl(Ioc ioc) {
        this.ioc = ioc;
    }

    @Override
    public <V> V read(Class<V> type, ReaderContext context) {
        final WebletTeleReader<V> reader = ioc.instance(new ClassedKey<>(WebletTeleReader.class, type), null);
        return reader.read(context);
    }

    @Override
    public <V> void write(Class<V> type, V value, WriterContext context) {
        final WebletTeleWriter<V> writer = ioc.instance(new ClassedKey<>(WebletTeleWriter.class, type), null);
        writer.write(value, context);
    }

}