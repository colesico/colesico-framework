/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import javax.inject.Provider;

@Service
public class AppService {

    private final Provider<Handle> handleProv;

    @Inject
    public AppService(Provider<Handle> handleProv) {
        this.handleProv = handleProv;
    }

    @Transactional
    public String readValue(Integer key) {

        Handle handle = handleProv.get();
        String val = handle.createQuery("select avalue from avalues where akey=:key")
                .bind("key", key)
                .mapTo(String.class)
                .first();

        return val;
    }
}
