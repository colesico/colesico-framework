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

package colesico.framework.service.codegen.modulator;

import colesico.framework.assist.spi.ServiceLocator;
import colesico.framework.assist.spi.ServiceLocatorFactory;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.ServiceMethodElement;
import colesico.framework.service.codegen.model.teleapi.TeleFacadeElement;
import colesico.framework.service.codegen.model.teleapi.TeleMethodElement;
import colesico.framework.service.codegen.model.teleapi.TeleInputElement;
import colesico.framework.service.codegen.parser.RoundContext;
import colesico.framework.service.codegen.parser.ServiceProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Modulators management
 */
public class ModulatorManager {
    protected final List<Modulator> modulators;
    protected final Set<Class<? extends Annotation>> serviceAnnotations;

    protected final Logger logger = LoggerFactory.getLogger(ModulatorManager.class);

    protected final ServiceLocatorFactory serviceLocatorFactory = ServiceLocatorFactory.of();

    public ModulatorManager() {
        modulators = new ArrayList<>();
        serviceAnnotations = new HashSet<>();
    }

    public void lookup() {
        modulators.clear();
        serviceAnnotations.clear();

        // Lookup modulators with ServiceLocator

        ServiceLocator<Modulator> modulators = serviceLocatorFactory.locator(this.getClass(), Modulator.class, getClass().getClassLoader());
        for (Modulator ext : modulators) {
            this.modulators.add(ext);
            Set<Class<? extends Annotation>> ma = ext.serviceAnnotations();
            if (ma != null) {
                serviceAnnotations.addAll(ma);
            }
            logger.debug("Found service modulator: " + ext.getClass().getName());
        }

        // Sort by event listening order

        this.modulators.sort((m1, m2) -> {
            switch (m1.listenOrder(m2.getClass())) {
                case BEFORE:
                    return -1;
                case AFTER:
                    return 1;
                default:
                    switch (m2.listenOrder(m1.getClass())) {
                        case BEFORE:
                            return 1;
                        case AFTER:
                            return -1;
                        default:
                            return 0;
                    }
            }
        });
    }

    public Set<Class<? extends Annotation>> serviceAnnotations() {
        return serviceAnnotations;
    }

    public void notifyInit(ServiceProcessorContext context) {
        for (Modulator modulator : modulators) {
            modulator.onInit(context);
        }
    }

    public void notifyRoundStart(RoundContext context) {
        for (Modulator modulator : modulators) {
            modulator.onRoundStart(context);
        }
    }

    public void notifyRoundStop() {
        for (Modulator modulator : modulators) {
            modulator.onRoundStop();
        }
    }

    public void notifyBeforeParseService(ServiceElement service) {
        for (Modulator modulator : modulators) {
            modulator.onBeforeParseService(service);
        }
    }

    public void notifyServiceMethodParsed(ServiceMethodElement serviceMethod) {
        for (Modulator modulator : modulators) {
            modulator.onServiceMethodParsed(serviceMethod);
        }
    }

    public void notifyInitTeleFacade(ServiceElement service) {
        for (Modulator modulator : modulators) {
            modulator.onInitTeleFacade(service);
        }
    }

    public void notifyBeforeParseTeleFacade(TeleFacadeElement teleFacade) {
        for (Modulator modulator : modulators) {
            modulator.onBeforeParseTeleFacade(teleFacade);
        }
    }

    public void notifyBeforeParseTeleMethod(TeleMethodElement teleMethod) {
        for (Modulator modulator : modulators) {
            modulator.onBeforeParseTeleMethod(teleMethod);
        }
    }

    public void notifyTeleInputParsed(TeleInputElement teleInput) {
        for (Modulator modulator : modulators) {
            modulator.onTeleInputParsed(teleInput);
        }
    }

    public void notifyTeleMethodParsed(TeleMethodElement teleMethod) {
        for (Modulator modulator : modulators) {
            modulator.onTeleMethodParsed(teleMethod);
        }
    }

    public void notifyTeleFacadeParsed(TeleFacadeElement teleFacade) {
        for (Modulator modulator : modulators) {
            modulator.onTeleFacadeParsed(teleFacade);
        }
    }

    public void notifyServiceParsed(ServiceElement service) {
        for (Modulator modulator : modulators) {
            modulator.onServiceParsed(service);
        }
    }

    public void notifyServiceGenerated(ServiceElement service) {
        for (Modulator modulator : modulators) {
            modulator.onServiceGenerated(service);
        }
    }

    public void notifyGenerateIocProducer(ProducerGenerator generator, ServiceElement serviceElm) {
        for (Modulator modulator : modulators) {
            modulator.onGenerateIocProducer(generator, serviceElm);
        }
    }

}
