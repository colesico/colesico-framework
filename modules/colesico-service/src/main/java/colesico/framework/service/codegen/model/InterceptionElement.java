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

package colesico.framework.service.codegen.model;

import com.squareup.javapoet.CodeBlock;

public class InterceptionElement {
    private final CodeBlock interceptor;
    private final CodeBlock parameters;

    public InterceptionElement(CodeBlock interceptor, CodeBlock parameters) {
        this.interceptor = interceptor;
        this.parameters = parameters;
    }

    public InterceptionElement(CodeBlock interceptor) {
        this.interceptor = interceptor;
        this.parameters = null;
    }

    public CodeBlock getInterceptor() {
        return interceptor;
    }

    public CodeBlock getParameters() {
        return parameters;
    }
}
