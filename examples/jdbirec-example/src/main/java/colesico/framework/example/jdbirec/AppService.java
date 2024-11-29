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
import colesico.framework.example.jdbirec.view.UserRK;
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


    private final UserRK userDao;

    @Named(Record.VIEW_FULL)
    private final UserRK userDaoFull;

    @Named(Record.VIEW_BRIEF)
    private final UserRK userDaoBrief;

    public AppService(Provider<Handle> handleProv, UserRK userDao, UserRK userDaoFull, UserRK userDaoBrief) {
        this.handleProv = handleProv;
        this.userDao = userDao;
        this.userDaoFull = userDaoFull;
        this.userDaoBrief = userDaoBrief;
    }

    public User getUser() {

        Handle handle = handleProv.get();

        String query = "select @record from @usr";

        Optional<User> user = handle
                .createQuery(userDao.sql(query))

                .map(userDao.mapper())
                .findFirst();


        return user.orElse(null);
    }
}
