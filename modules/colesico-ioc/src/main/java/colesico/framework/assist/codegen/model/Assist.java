/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

package colesico.framework.assist.codegen.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Basic tooled element
 */
abstract public class Assist {

    protected final ProcessingEnvironment processingEnv;

    public Assist(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    protected ProcessingEnvironment processingEnv() {
        return processingEnv;
    }

    protected Elements elementUtils() {
        return processingEnv.getElementUtils();
    }

    protected Types typeUtils() {
        return processingEnv.getTypeUtils();
    }

}
