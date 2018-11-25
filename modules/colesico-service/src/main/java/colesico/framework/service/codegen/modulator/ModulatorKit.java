/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.service.codegen.modulator;

import colesico.framework.assist.ServiceLocator;
import colesico.framework.service.Service;
import colesico.framework.service.codegen.model.*;
import colesico.framework.service.codegen.parser.ProcessorContext;
import colesico.framework.service.codegen.parser.RoundContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author Vladlen Larionov
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

        ServiceLocator<Modulator> locator = ServiceLocator.of(this.getClass(), Modulator.class, getClass().getClassLoader());
        for (Modulator ext : locator.getProviders()) {
            modulators.add(ext);
            Set<Class<? extends Annotation>> ma = ext.serviceAnnotations();
            if (ma != null) {
                serviceAnnotations.addAll(ma);
            }
            logger.debug("Found modulator: " + ext.getClass().getName());
        }
    }

    public Set<Class<? extends Annotation>> getServiceAnnotations() {
        return serviceAnnotations;
    }

    public void notifyInit(ProcessorContext context) {
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

    public void notifyService(ServiceElement service) {
        for (Modulator modulator : modulators) {
            modulator.onService(service);
        }
    }

    public void notifyProxyMethod(ProxyMethodElement proxyMethod) {
        for (Modulator modulator : modulators) {
            modulator.onProxyMethod(proxyMethod);
        }
    }

    public void notifyAddTeleFacade(ServiceElement service) {
        for (Modulator modulator : modulators) {
            modulator.onAddTeleFacade(service);
        }
    }

    public void notifyAddTeleMethod(TeleMethodElement teleMethod) {
        for (Modulator modulator : modulators) {
            modulator.onAddTeleMethod(teleMethod);
        }
    }

    public void notifyLinkTeleParam(TeleParamElement teleParam, Deque<VariableElement> varStack) {
        for (Modulator modulator : modulators) {
            modulator.onLinkTeleParam(teleParam, varStack);
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
}
