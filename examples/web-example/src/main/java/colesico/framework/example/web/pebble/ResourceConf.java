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

package colesico.framework.example.web.pebble;

import colesico.framework.config.Config;
import colesico.framework.resource.ResourceOptionsPrototype;
import colesico.framework.resource.RewriterRegistry;
import colesico.framework.resource.rewriters.PropertyRewriter;

import javax.inject.Inject;

@Config
public class ResourceConf extends ResourceOptionsPrototype {

    private PropertyRewriter rewriter;

    @Inject
    public ResourceConf(PropertyRewriter rewriter) {
        this.rewriter = rewriter;
    }

    @Override
    public void setupRewriters(RewriterRegistry registry) {
        rewriter.register(registry);
        rewriter.property("$tmplRoot", "colesico/framework/example/web/pebble/tmpl");
    }
}
