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

package colesico.framework.webstatic;


import colesico.framework.resource.ResourceKit;
import org.apache.commons.lang3.StringUtils;

/**
 * Static static content sender
 *
 * @author Vladlen Larionov
 */
public interface StaticContent {

    String L10N_MODE_PARAM = "l10n";

    void send(String resourceUri, ResourceKit.L10NMode mode);

    default void send(String resourceUri, String l10nMode) {
        if (StringUtils.isBlank(l10nMode)) {
            send(resourceUri, ResourceKit.L10NMode.NONE);
        } else {
            switch (l10nMode) {
                case "dir":
                    send(resourceUri, ResourceKit.L10NMode.DIR);
                    break;
                case "file":
                    send(resourceUri, ResourceKit.L10NMode.FILE);
                    break;
                case "none":
                    send(resourceUri, ResourceKit.L10NMode.NONE);
                    break;
                default:
                    throw new RuntimeException("Unsupported localization mode: " + l10nMode);
            }
        }
    }

    interface Builder {

        String DEFAULT_RESOURCES_DIR = "webpub";

        Builder resourcesRoot(String path);

        StaticContent build();
    }
}
