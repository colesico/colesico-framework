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

import colesico.framework.ioc.conditional.Requires;
import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.conditional.TestCondition;
import colesico.framework.ioc.production.Polyproduce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileUtils;
import colesico.framework.resource.ResourceOptionsPrototype;
import colesico.framework.resource.rewriters.ParamRewriter;

import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;

@Producer
@Requires(TestCondition.class)
@Substitute
public class TestProducer {

    @Singleton
    public Profile getProfile(ProfileUtils profileUtils) {
        return profileUtils.create(List.of(Locale.of("en", "RU")), null);

    }

    @Singleton
    @Polyproduce
    public ResourceOptionsPrototype getResourceConfig() {
        ParamRewriter rewriter = new ParamRewriter();
        return new ResourcesOptions(rewriter);
    }
}

