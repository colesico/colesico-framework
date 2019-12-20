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

package colesico.framework.translation.codegen.model;

import colesico.framework.config.codegen.ConfigParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictionaryRegistry {
    private Logger logger = LoggerFactory.getLogger(ConfigParser.class);

    protected final ProcessingEnvironment processingEnv;
    protected final Elements elementUtils;
    protected final Map<String, List<DictionaryElement>> byPackageMap = new HashMap<>();

    public DictionaryRegistry(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        this.elementUtils = processingEnv.getElementUtils();
    }

    public boolean isEmpty() {
        return byPackageMap.isEmpty();
    }

    public void clear() {
        byPackageMap.clear();
    }

    public Map<String, List<DictionaryElement>> getByPackageMap() {
        return byPackageMap;
    }


    public DictionaryElement register(DictionaryElement dictionaryElement) {

        String packageName = dictionaryElement.getOriginBean().getPackageName();

        List<DictionaryElement> packageDictBeans = byPackageMap.computeIfAbsent(packageName, k -> new ArrayList<>());
        packageDictBeans.add(dictionaryElement);

        logger.debug("Dictionary bean " + dictionaryElement.getOriginBean().asType().toString() + " has been registered");
        return dictionaryElement;
    }
}
