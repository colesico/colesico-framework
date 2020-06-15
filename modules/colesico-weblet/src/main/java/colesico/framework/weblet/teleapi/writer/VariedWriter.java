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

package colesico.framework.weblet.teleapi.writer;

import colesico.framework.weblet.VariedResponse;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.WebletTWContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class VariedWriter implements WebletTeleWriter<VariedResponse> {

    private final NavigationWriter navigationWriter;
    private final StringWriter stringWriter;
    private final BinaryWriter binaryWriter;

    @Inject
    public VariedWriter(NavigationWriter navigationWriter,
                        StringWriter stringWriter,
                        BinaryWriter binaryWriter) {
        this.navigationWriter = navigationWriter;
        this.stringWriter = stringWriter;
        this.binaryWriter = binaryWriter;
    }

    @Override
    public void write(VariedResponse value, WebletTWContext wrContext) {
        if (value.getNavigationResponse() != null) {
            navigationWriter.write(value.getNavigationResponse(), wrContext);
        } else if (value.getStringResponse() != null) {
            stringWriter.write(value.getStringResponse(), wrContext);
        } else if (value.getBinaryResponse() != null) {
            binaryWriter.write(value.getBinaryResponse(), wrContext);
        } else {
            throw new RuntimeException("Undefined weblet response");
        }
    }
}
