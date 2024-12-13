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

package colesico.framework.example.rpc;

import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.internal.ProfileImpl;
import colesico.framework.profile.Profile;
import colesico.framework.security.DefaultPrincipal;
import colesico.framework.security.Principal;

import java.util.Locale;

/**
 * This producer is used to mock up a Principal and profile producing
 */
@Producer
public class MockProducer {

    @Substitute
    public Profile getProfile() {
        return new ProfileImpl(new Locale("de", "DE", "UNIX"));
    }

    @Substitute
    public Principal getPrincipal() {
        return new DefaultPrincipal("ID0");
    }
}
