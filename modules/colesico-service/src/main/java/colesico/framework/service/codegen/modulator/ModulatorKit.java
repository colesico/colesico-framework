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

package colesico.framework.service.codegen.modulator;

import colesico.framework.assist.ServiceLocator;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.parser.ServiceProcessorContext;
import colesico.framework.service.codegen.parser.RoundContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Modulators management
 */
public class ModulatorKit {
    protected final List<Modulator> modulators;
    protected final Set<Class<? extends Annotation>> serviceAnnotations;

    protected final Logger logger = LoggerFactory.getLogger(ModulatorKit.class);

    public ModulatorKit() {
        modulators = new ArrayList<>();
        serviceAnnotations = new HashSet<>();
    }

    public void lookup() {
        modulators.clear();
        serviceAnnotations.clear();

        // Lookup modulators with ServiceLocator

        ServiceLocator<Modulator> locator = ServiceLocator.of(this.getClass(), Modulator.class, getClass().getClassLoader());
        for (Modulator ext : locator.getProviders()) {
            modulators.add(ext);
            Set<Class<? extends Annotation>> ma = ext.serviceAnnotations();
            if (ma != null) {
                serviceAnnotations.addAll(ma);
            }
            logger.debug("Found modulator: " + ext.getClass().getName());
        }

        // Sort by event listening order

        modulators.sort((m1, m2) -> {
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

    public Set<Class<? extends Annotation>> getServiceAnnotations() {
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
