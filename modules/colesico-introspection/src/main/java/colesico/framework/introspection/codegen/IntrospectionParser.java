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

package colesico.framework.introspection.codegen;

import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.introspection.codegen.model.IntrospectedElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * @author Vladlen Larionov
 */
public class IntrospectionParser extends FrameworkAbstractParser {

    private Logger logger = LoggerFactory.getLogger(IntrospectionParser.class);

    public IntrospectionParser(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }


    public IntrospectedElement parse(ClassElement target) {
        return null;
    }

}
