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

package colesico.framework.ioc.internal;

import colesico.framework.assist.ServiceLocator;
import colesico.framework.ioc.*;
import colesico.framework.ioc.ioclet.AdvancedIoc;
import colesico.framework.ioc.ioclet.Factory;
import colesico.framework.ioc.ioclet.Ioclet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static colesico.framework.ioc.internal.LazyIocContainer.CIRCULAR_DEP_ERR_MSG;

/**
 * @author Vladlen Larionov
 */

public class IocBuilderImpl implements IocBuilder {

    private static final Logger log = LoggerFactory.getLogger(IocBuilder.class);

    /**
     * Manually added ioclets
     */
    protected List<Ioclet> extraIoclets = new ArrayList<>();

    /**
     * Manually added ranks;
     */
    protected List<String> extraRanks = new ArrayList<>();

    protected Set<String> ignoredProducers = new HashSet<>();

    protected boolean useDefaultRanks;

    protected boolean iocletsDiscovery;

    protected boolean preactivation;

    protected ContainerType iocType;

    public IocBuilderImpl() {
        useDefaultRanks = true;
        iocletsDiscovery = true;
        preactivation = true;
        iocType = ContainerType.EAGER;
    }

    @Override
    public IocBuilder useRank(String name) {
        extraRanks.add(name);
        return this;
    }

    @Override
    public IocBuilder disableDefaultRanks() {
        useDefaultRanks = false;
        return this;
    }

    @Override
    public IocBuilder disableIocletsDiscovery() {
        this.iocletsDiscovery = false;
        return this;
    }

    @Override
    public IocBuilder useIoclet(Ioclet ioclet) {
        extraIoclets.add(ioclet);
        return this;
    }

    @Override
    public IocBuilder disablePreactivation() {
        this.preactivation = false;
        return this;
    }

    @Override
    public IocBuilder useContainerType(ContainerType val) {
        this.iocType = val;
        return this;
    }

    @Override
    public IocBuilder ignoreProducer(String producerId) {
        ignoredProducers.add(producerId);
        return this;
    }

    @Override
    public Ioc build() {
        IocletRanker ranker = new IocletRanker();

        if (useDefaultRanks) {
            log.debug("Use default ranks: on");
            ranker.pushRank(Rank.RANK_MINOR);
            ranker.pushRank(Rank.RANK_DEFAULT);
            ranker.pushRank(Rank.RANK_EXTENSION);
        } else {
            log.debug("Use default ranks: off");
        }

        for (String rank : extraRanks) {
            ranker.pushRank(rank);
        }

        // Log ranks stack
        if (log.isDebugEnabled()) {
            StringBuilder rsb = new StringBuilder("IoC ranks stack: ");
            for (IocletRanker.RankItem ri : ranker.getRankStack()) {
                rsb.append(ri.getRank()).append("; ");
            }
            log.debug(rsb.toString());
        }

        if (ignoredProducers.isEmpty()) {
            if (iocletsDiscovery) {
                log.debug("Ioclets discovery: on");
                List<Ioclet> foundList = lookupIoclets();
                for (Ioclet ioclet : foundList) {
                    ranker.addIoclet(ioclet);
                }
            } else {
                log.debug("Ioclets discovery: off");
            }
            for (Ioclet ioclet : extraIoclets) {
                ranker.addIoclet(ioclet);
            }
        } else {
            log.debug("Has ignored IoC producers");
            if (iocletsDiscovery) {
                log.debug("Ioclets discovery: on");
                List<Ioclet> foundList = lookupIoclets();
                for (Ioclet ioclet : foundList) {
                    if (!ignoredProducers.contains(ioclet.getProducerId())) {
                        ranker.addIoclet(ioclet);
                    }
                }
            } else {
                log.debug("Ioclets discovery: off");
            }
            for (Ioclet ioclet : extraIoclets) {
                if (!ignoredProducers.contains(ioclet.getProducerId())) {
                    ranker.addIoclet(ioclet);
                }
            }
        }


        Map<Key<?>, Factory<?>> factories = ranker.toFactories();

        AdvancedIoc ioc;

        log.debug("IoC type: " + iocType.name());
        switch (iocType) {
            case EAGER:
                ioc = new EagerIocContainer(factories);
                break;
            case LAZY:
                ioc = new LazyIocContainer(factories);
                break;
            default:
                throw new IocException("Unsupported IoC container type");
        }

        if (preactivation) {
            log.debug("Preactivation: on");
            activateFactories(ioc, factories);
        } else {
            log.debug("Preactivation: off");
        }

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
            throw new IocException(String.format(CIRCULAR_DEP_ERR_MSG, currentKey!=null?currentKey.toString():"?"));
        }
    }

    protected List<Ioclet> lookupIoclets() {
        List<Ioclet> result = new ArrayList<>();
        log.debug("Lookup ioclets...");
        ServiceLocator<Ioclet> locator = ServiceLocator.of(this.getClass(), Ioclet.class);
        for (Ioclet ioclet : locator.getProviders()) {
            log.debug("Found ioclet '" + ioclet.getClass().getName() + "' for producer '" + ioclet.getProducerId() + "' with rank '" + ioclet.getRank() + "'");
            result.add(ioclet);
        }
        return result;
    }

}