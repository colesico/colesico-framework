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

package colesico.framework.example.jdbirec;

import colesico.framework.example.jdbirec.view.User;
import colesico.framework.example.jdbirec.view.UserRk;
import colesico.framework.jdbirec.Record;
import colesico.framework.service.Service;
import colesico.framework.transaction.Transactional;
import org.jdbi.v3.core.Handle;

import javax.inject.Named;
import javax.inject.Provider;
import java.util.Optional;

@Service
@Transactional
public class AppService {

    /**
     * JDBI handle provider
     */
    private final Provider<Handle> handleProv;


    private final UserRk userRk;

    private final UserRk userFullRk;

    private final UserRk userBriefRk;

    public AppService(Provider<Handle> handleProv,
                      UserRk userDao,
                      @Named(Record.VIEW_FULL)
                      UserRk userDaoFull,
                      @Named(Record.VIEW_BRIEF)
                      UserRk userDaoBrief) {

        this.handleProv = handleProv;
        this.userRk = userDao;
        this.userFullRk = userDaoFull;
        this.userBriefRk = userDaoBrief;
    }

    public User getUser() {

        Handle handle = handleProv.get();

        String query = "select @record from @usr";

        Optional<User> user = handle
                .createQuery(userRk.sql(query))
                .map(userRk.mapper())
                .findFirst();


        return user.orElse(null);
    }

    public User getUserFull() {

        Handle handle = handleProv.get();

        String query = "select @record from @usr";

        Optional<User> user = handle
                .createQuery(userFullRk.sql(query))
                .map(userFullRk.mapper())
                .findFirst();

        return user.orElse(null);
    }

    public User getUserBrief() {

        Handle handle = handleProv.get();

        String query = "select @record from @usr where id=:id";

        Optional<User> user = handle
                .createQuery(userBriefRk.sql(query))
                .bind("id", 1)
                .map(userBriefRk.mapper())
                .findFirst();

        return user.orElse(null);
    }
}
