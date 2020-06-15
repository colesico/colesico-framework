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

package colesico.framework.weblet.teleapi;

/**
 * @author Vladlen Larionov
 * Weblet tele-writing context stub
 */
public final class WebletTWContext {

    /**
     * Custom writer class or null.
     * If null - default writer will be used
     */
    private final Class<? extends WebletTeleWriter> writerClass;

    public WebletTWContext(Class<? extends WebletTeleWriter> writerClass) {
        this.writerClass = writerClass;
    }

    public WebletTWContext() {
        this.writerClass = null;
    }

    public Class<? extends WebletTeleWriter> getWriterClass() {
        return writerClass;
    }
}
