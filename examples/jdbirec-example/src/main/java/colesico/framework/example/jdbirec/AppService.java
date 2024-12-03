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

import colesico.framework.example.jdbirec.selectas.SAUser;
import colesico.framework.example.jdbirec.selectas.SAUserRk;
import colesico.framework.example.jdbirec.view.VWUser;
import colesico.framework.example.jdbirec.view.VWUserRk;
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


    private final VWUserRk vwUserRk;

    private final VWUserRk vwUserFullRk;

    private final VWUserRk vwUserBriefRk;

    private final SAUserRk saUserRk;

    public AppService(Provider<Handle> handleProv,
                      SAUserRk saUserRk,
                      VWUserRk vwUserRk,
                      @Named(Record.VIEW_FULL)
                      VWUserRk vwUserFullRk,
                      @Named(Record.VIEW_BRIEF)
                      VWUserRk vwUserBriefRk) {

        this.handleProv = handleProv;

        this.saUserRk = saUserRk;

        this.vwUserRk = vwUserRk;
        this.vwUserFullRk = vwUserFullRk;
        this.vwUserBriefRk = vwUserBriefRk;
    }

    public SAUser getSAUser() {

        Handle handle = handleProv.get();

        String query = saUserRk.sql("select count(*) as count, @record from @usr");
        System.out.println("SA: " + query);

        Optional<SAUser> user = handle
                .createQuery(query)
                .map(saUserRk.mapper())
                .findFirst();


        return user.orElse(null);
    }


    public VWUser getVWUser() {

        Handle handle = handleProv.get();

        String query = vwUserRk.sql("select @record from @usr");
        System.out.println("VW: " + query);
        Optional<VWUser> user = handle
                .createQuery(query)
                .map(vwUserRk.mapper())
                .findFirst();


        return user.orElse(null);
    }

    public VWUser getVWUserFull() {

        Handle handle = handleProv.get();

        String query = "select @record from @usr";

        Optional<VWUser> user = handle
                .createQuery(vwUserFullRk.sql(query))
                .map(vwUserFullRk.mapper())
                .findFirst();

        return user.orElse(null);
    }

    public VWUser getVWUserBrief() {

        Handle handle = handleProv.get();

        String query = "select @record from @usr where id=:id";

        Optional<VWUser> user = handle
                .createQuery(vwUserBriefRk.sql(query))
                .bind("id", 1)
                .map(vwUserBriefRk.mapper())
                .findFirst();

        return user.orElse(null);
    }
}
