/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.example.config;

import colesico.framework.example.config.message.TargetBean;
import colesico.framework.example.config.message.MessageConfig1;
import colesico.framework.example.config.message.MessageConfig2;
import colesico.framework.example.config.polyvariant.PolyConfigPrototype;
import colesico.framework.example.config.simple.SimpleConfig;
import colesico.framework.example.config.single.SingleConfigPrototype;
import colesico.framework.example.config.source.SourcePrefixConfig;
import colesico.framework.example.config.source.SourceSimpleConfig;
import colesico.framework.example.config.source.SourceSingleConfigPrototype;
import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Polysupplier;

import javax.inject.Provider;

public class MainBean {

    private final SimpleConfig simpleConfig;
    private final Provider<SingleConfigPrototype> singleConfigProv;
    private final Polysupplier<PolyConfigPrototype> polyConfig;

    private final TargetBean targetService1;
    private final TargetBean targetService2;

    private final SourceSimpleConfig sourceSimpleConfig;
    private final SourceSingleConfigPrototype sourceSingleConfig;
    private final SourcePrefixConfig sourcePrefixConfig;

    public MainBean(SimpleConfig simpleConfig,
                    Provider<SingleConfigPrototype> singleConfigProv,
                    Polysupplier<PolyConfigPrototype> polyConfig,

                    @Classed(MessageConfig1.class)
                        TargetBean targetService1,
                    @Classed(MessageConfig2.class)
                        TargetBean targetService2,

                    SourceSimpleConfig sourceSimpleConfig,
                    SourceSingleConfigPrototype sourceSingleConfig,
                    SourcePrefixConfig sourcePrefixConfig
    ) {

        this.simpleConfig = simpleConfig;
        this.singleConfigProv = singleConfigProv;
        this.polyConfig = polyConfig;
        this.targetService1 = targetService1;
        this.targetService2 = targetService2;
        this.sourceSimpleConfig = sourceSimpleConfig;
        this.sourceSingleConfig = sourceSingleConfig;
        this.sourcePrefixConfig = sourcePrefixConfig;
    }

    public String getSimpleConfigValue() {
        return simpleConfig.getValue();
    }

    public String getSingleConfigValue() {
        return singleConfigProv.get().getValue() + ";" + singleConfigProv.get().getValue();
    }

    public String getPolyconfigValues() {
        final StringBuilder sb = new StringBuilder();
        polyConfig.forEach(cfg -> sb.append(cfg.getValue()).append(";"), null);
        return sb.toString();
    }

    public String getMessageConfigValues() {
        return targetService1.getValue() + ";" + targetService2.getValue();
    }

    public String getSourceSimpleConfigValue() {
        return sourceSimpleConfig.getValue() + ";" + sourceSimpleConfig.getDefaultValue();
    }

    public String getSourceSingleConfigValue() {
        return sourceSingleConfig.getValue();
    }

    public String getSourcePrefixConfigValue() {
        return sourcePrefixConfig.getValue();
    }

}
