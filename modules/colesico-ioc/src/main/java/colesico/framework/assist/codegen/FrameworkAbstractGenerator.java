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

package colesico.framework.assist.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Map;

public class FrameworkAbstractGenerator {
    protected final Logger logger;
    protected final ProcessingEnvironment processingEnv;

    public FrameworkAbstractGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    protected ProcessingEnvironment getProcessingEnv() {
        return processingEnv;
    }

    protected Elements getElementUtils() {
        return processingEnv.getElementUtils();
    }

    protected Messager getMessager() {
        return processingEnv.getMessager();
    }

    protected Filer getFiler() {
        return processingEnv.getFiler();
    }

    protected Types getTypeUtils() {
        return processingEnv.getTypeUtils();
    }

    protected Map<String, String> getOptions() {
        return processingEnv.getOptions();
    }

    protected CodegenMode getCodegenMode() {
        String codegenModeKey = processingEnv.getOptions().get(CodegenUtils.OPTION_CODEGEN);
        return CodegenMode.fromKey(codegenModeKey);
    }
}
