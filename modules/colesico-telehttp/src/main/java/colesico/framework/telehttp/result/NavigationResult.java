/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.telehttp.result;


import colesico.framework.httprouter.assist.Navigation;
import colesico.framework.teleapi.TeleResult;

/**
 * {@link Navigation} wrapper
 * to perform {@link colesico.framework.httprouter.Router} navigation
 */
public record NavigationResult(Navigation navigation) implements TeleResult {

    public static NavigationResult of(Navigation navigation) {
        return new NavigationResult(navigation);
    }

    public static NavigationResult redirect(String uri) {
        return new NavigationResult(Navigation.of(uri).action(Navigation.Action.REDIRECT));
    }

    public static NavigationResult forward(String uri) {
        return new NavigationResult(Navigation.of(uri).action(Navigation.Action.FORWARD));
    }

    public static NavigationResult redirect(Class<?> serviceClass, String serviceMethod) {
        return new NavigationResult(Navigation.of(serviceClass, serviceMethod).action(Navigation.Action.REDIRECT));
    }

    public static NavigationResult forward(Class<?> serviceClass, String serviceMethod) {
        return new NavigationResult(Navigation.of(serviceClass, serviceMethod).action(Navigation.Action.FORWARD));
    }
}
