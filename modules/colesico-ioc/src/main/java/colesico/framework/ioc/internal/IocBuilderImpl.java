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

package colesico.framework.ioc.internal;

import colesico.framework.ioc.*;
import colesico.framework.ioc.ioclet.AdvancedIoc;
import colesico.framework.ioc.ioclet.Factory;
import colesico.framework.ioc.ioclet.Ioclet;
import colesico.framework.assist.ServiceLocator;
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
            ranker.pushRank(Rank.RANK_MINOR);
            ranker.pushRank(Rank.RANK_DEFAULT);
            ranker.pushRank(Rank.RANK_EXTENSION);
        }

        for (String rank : extraRanks) {
            ranker.pushRank(rank);
        }

        // Log ranks stack
        if (log.isDebugEnabled()) {
            StringBuilder rsb = new StringBuilder("Ioc ranks stack: ");
            for (IocletRanker.RankItem ri : ranker.getRankStack()) {
                rsb.append(ri.getRank()).append("; ");
            }
            log.debug(rsb.toString());
        }


        if (ignoredProducers.isEmpty()) {
            if (iocletsDiscovery) {
                List<Ioclet> foundList = lookupIoclets();
                for (Ioclet ioclet : foundList) {
                    ranker.addIoclet(ioclet);
                }
            }
            for (Ioclet ioclet : extraIoclets) {
                ranker.addIoclet(ioclet);

            }
        } else {
            if (iocletsDiscovery) {
                List<Ioclet> foundList = lookupIoclets();
                for (Ioclet ioclet : foundList) {
                    if (!ignoredProducers.contains(ioclet.getProducerId())) {
                        ranker.addIoclet(ioclet);
                    }
                }
            }
            for (Ioclet ioclet : extraIoclets) {
                if (!ignoredProducers.contains(ioclet.getProducerId())) {
                    ranker.addIoclet(ioclet);
                }
            }
        }


        Map<Key<?>, Factory<?>> factories = ranker.toFactories();

        AdvancedIoc ioc;

        switch (iocType) {
            case EAGER:
                ioc = new EagerIocContainer(factories);
                break;
            case LAZY:
                ioc = new LazyIocContainer(factories);
                break;
            default:
                ioc = new EagerIocContainer(factories);
        }

        if (preactivation) {
            activateFactories(ioc, factories);
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
            throw new IocException("Circular dependence for key: " + currentKey);
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