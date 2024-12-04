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

import colesico.framework.example.jdbirec.renaming.RUser;
import colesico.framework.example.jdbirec.renaming.RUserRk;
import colesico.framework.example.jdbirec.selectas.SUser;
import colesico.framework.example.jdbirec.selectas.SUserRk;
import colesico.framework.example.jdbirec.view.VUser;
import colesico.framework.example.jdbirec.view.VUserRk;
import colesico.framework.jdbirec.RecordView;
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


    private final VUserRk vUserRk;

    private final VUserRk vUserFullRk;

    private final VUserRk vUserBriefRk;

    private final SUserRk sUserRk;

    private final RUserRk rUserRk;

    public AppService(Provider<Handle> handleProv,
                      RUserRk rUserRk,
                      SUserRk sUserRk,
                      VUserRk vUserRk,
                      @Named(RecordView.VIEW_FULL)
                      VUserRk vUserFullRk,
                      @Named(RecordView.VIEW_BRIEF)
                      VUserRk vUserBriefRk) {

        this.handleProv = handleProv;

        this.rUserRk = rUserRk;
        this.sUserRk = sUserRk;

        this.vUserRk = vUserRk;
        this.vUserFullRk = vUserFullRk;
        this.vUserBriefRk = vUserBriefRk;
    }

    public RUser getRUser() {

        Handle handle = handleProv.get();

        String query = rUserRk.sql("select @record from @usr");
        System.out.println("RU: " + query);

        Optional<RUser> user = handle
                .createQuery(query)
                .map(rUserRk.mapper())
                .findFirst();


        return user.orElse(null);
    }

    public SUser getSUser() {

        Handle handle = handleProv.get();

        String query = sUserRk.sql("select count(*) as count, @record from @usr");
        System.out.println("SU: " + query);

        Optional<SUser> user = handle
                .createQuery(query)
                .map(sUserRk.mapper())
                .findFirst();


        return user.orElse(null);
    }


    public VUser getVUser() {

        Handle handle = handleProv.get();

        String query = vUserRk.sql("select @record from @usr");
        System.out.println("VU: " + query);
        Optional<VUser> user = handle
                .createQuery(query)
                .map(vUserRk.mapper())
                .findFirst();


        return user.orElse(null);
    }

    public VUser getVUserFull() {

        Handle handle = handleProv.get();

        String query = "select @record from @usr";

        Optional<VUser> user = handle
                .createQuery(vUserFullRk.sql(query))
                .map(vUserFullRk.mapper())
                .findFirst();

        return user.orElse(null);
    }

    public VUser getVUserBrief() {

        Handle handle = handleProv.get();

        String query = "select @record from @usr where id=:id";

        Optional<VUser> user = handle
                .createQuery(vUserBriefRk.sql(query))
                .bind("id", 1)
                .map(vUserBriefRk.mapper())
                .findFirst();

        return user.orElse(null);
    }
}
