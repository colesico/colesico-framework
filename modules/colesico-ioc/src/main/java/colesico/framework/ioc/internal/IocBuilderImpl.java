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

package colesico.framework.ioc.internal;

import colesico.framework.assist.ServiceLocator;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.IocException;
import colesico.framework.ioc.conditional.Condition;
import colesico.framework.ioc.conditional.ConditionContext;
import colesico.framework.ioc.ioclet.AdvancedIoc;
import colesico.framework.ioc.ioclet.Factory;
import colesico.framework.ioc.ioclet.Ioclet;
import colesico.framework.ioc.key.Key;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Vladlen Larionov
 */

public class IocBuilderImpl implements IocBuilder {

    private static final Logger log = LoggerFactory.getLogger(IocBuilder.class);

    /**
     * Manually added ioclets
     */
    protected List<Ioclet> extraIoclets = new ArrayList<>();

    protected boolean iocletsDiscovery;

    protected CatalogImpl catalog;

    protected ConditionContext conditionContext = new ConditionContextImpl();

    private IocBuilderImpl() {
        iocletsDiscovery = true;
    }

    public static IocBuilderImpl create() {
        return new IocBuilderImpl();
    }

    @Override
    public IocBuilderImpl disableIocletsDiscovery() {
        this.iocletsDiscovery = false;
        return this;
    }

    @Override
    public IocBuilderImpl useIoclet(Ioclet ioclet) {
        extraIoclets.add(ioclet);
        return this;
    }

    @Override
    public ConditionContext getConditionContext() {
        return conditionContext;
    }

    @Override
    public Ioc build() {

        catalog = new CatalogImpl(conditionContext);

        Ioclet curIoclet = null;
        try {
            if (iocletsDiscovery) {
                log.debug("Ioclets discovery: on");
                List<Ioclet> foundList = lookupIoclets();
                for (Ioclet ioclet : foundList) {
                    curIoclet = ioclet;
                    ioclet.addFactories(catalog);
                }
            } else {
                log.debug("Ioclets discovery: off");
            }

            for (Ioclet ioclet : extraIoclets) {
                curIoclet = ioclet;
                ioclet.addFactories(catalog);
            }
        } catch (Exception e) {
            throw new IocException("Error loading ioclet: " + (curIoclet != null ? curIoclet.getId() : "?") + "; message: " + ExceptionUtils.getRootCauseMessage(e));
        }

        Map<Key<?>, Factory<?>> factories = catalog.getFactories();

        AdvancedIoc ioc = new IocContainerImpl(factories);

        activateFactories(ioc, factories);

        return ioc;
    }

    protected void activateFactories(AdvancedIoc ioc, Map<Key<?>, Factory<?>> factories) {
        Key<?> currentKey = null;
        try {
            for (Map.Entry<Key<?>, Factory<?>> e : factories.entrySet()) {
                currentKey = e.getKey();
                Factory<?> supl = e.getValue();
                while (supl != null) {
                    supl.activate(ioc);
                    supl = supl.next();
                }
            }
        } catch (StackOverflowError soe) {
            throw new IocException(String.format("Possible circular dependencies", currentKey != null ? currentKey.toString() : "?"));
        }
    }

    protected List<Ioclet> lookupIoclets() {
        List<Ioclet> result = new ArrayList<>();
        log.debug("Lookup ioclets...");
        ServiceLocator<Ioclet> locator = ServiceLocator.of(this.getClass(), Ioclet.class);
        for (Ioclet ioclet : locator.getProviders()) {
            log.debug("Found ioclet '" + ioclet.getClass().getName() + "' with id: '" + ioclet.getId() + "'");
            result.add(ioclet);
        }
        return result;
    }

}