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

package colesico.framework.example.jdbi;

import colesico.framework.service.Service;
import colesico.framework.transaction.Transactional;
import org.jdbi.v3.core.Handle;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

@Service
public class AppService {

    /**
     * JDBI handle provider
     */
    private final Provider<Handle> defaultHndProv;
    private final Provider<Handle> extraHndProv;

    public AppService(Provider<Handle> defaultHndProv,
                      @Named(ExtraJdbiProducer.EXTRA)
                              Provider<Handle> extraHndProv) {
        this.defaultHndProv = defaultHndProv;
        this.extraHndProv = extraHndProv;
    }

    @Transactional(shell = ExtraJdbiProducer.EXTRA)
    public String readExtraValue(Integer key) {

        Handle handle = extraHndProv.get();
        String val = handle.createQuery("select avalue from avalues where akey=:key")
                .bind("key", key)
                .mapTo(String.class)
                .first();

        return val;
    }

    @Transactional
    public String readValue(Integer key) {

        Handle handle = defaultHndProv.get();
        String val = handle.createQuery("select avalue from avalues where akey=:key")
                .bind("key", key)
                .mapTo(String.class)
                .first();

        return val;
    }
}
