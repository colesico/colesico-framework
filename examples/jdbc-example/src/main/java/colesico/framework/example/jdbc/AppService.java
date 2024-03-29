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

package colesico.framework.example.jdbc;

import colesico.framework.service.Service;
import colesico.framework.transaction.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Service
public class AppService {

    /**
     * JDBC default connection provider
     */
    private final Provider<Connection> defaultConnProv;

    /**
     * JDBC extra connection provider
     */
    private final Provider<Connection> extraConnProv;

    @Inject
    public AppService(Provider<Connection> defaultConnProv,
                      @Named(ExtraJdbcProducer.EXTRA) Provider<Connection> extraConnProv) {
        this.defaultConnProv = defaultConnProv;
        this.extraConnProv = extraConnProv;
    }

    @Transactional(shell = ExtraJdbcProducer.EXTRA)
    public String readExtraValue(Integer key) {
        try (PreparedStatement stmt = extraConnProv.get().prepareStatement("select avalue from avalues where akey=?")) {
            stmt.setInt(1, key);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public String readValue(Integer key) {
        try (PreparedStatement stmt = defaultConnProv.get().prepareStatement("select avalue from avalues where akey=?")) {
            stmt.setInt(1, key);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
