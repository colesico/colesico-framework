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

package colesico.framework.test.resource;

import colesico.framework.ioc.production.Polyproduce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.DefaultProfile;
import colesico.framework.profile.Profile;
import colesico.framework.resource.ResourceOptionsPrototype;

import javax.inject.Singleton;
import java.util.Locale;

@Producer(TestTag.class)
public class TestProducer {

    @Singleton
    public Profile getProfile() {
        return new DefaultProfile(new Locale("en","RU"));
    }

    @Singleton
    @Polyproduce
    public ResourceOptionsPrototype getResourceConfig() {
        return new ResourcesConf();
    }
}

