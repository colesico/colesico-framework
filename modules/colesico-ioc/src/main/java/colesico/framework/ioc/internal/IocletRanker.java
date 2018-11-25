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

import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.IocException;
import colesico.framework.ioc.Key;
import colesico.framework.ioc.ioclet.Factory;
import colesico.framework.ioc.ioclet.Catalog;
import colesico.framework.ioc.ioclet.Ioclet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Sorts ioclets by rank and composes the resulting factories map
 *
 * @author Vladlen Larionov
 * @see IocletRanker#toFactories()
 */
public class IocletRanker {

    protected static final Logger log = LoggerFactory.getLogger(IocBuilder.class);

    protected final Map<String, RankItem> rankIndex = new LinkedHashMap<>();
    protected final Deque<RankItem> rankStack = new ArrayDeque<>();

    /**
     * Adds rank to rank's stack. Ranks stack head - maximum  priority rank, tail - lowest
     *
     * @param rank
     */
    public void pushRank(String rank) {
        RankItem rankItem = new RankItem(rank);
        RankItem oldItem = rankIndex.put(rank, rankItem);
        if (oldItem != null) {
            String errMsg = String.format("Rank '%s' is already in use", rank);
            log.error(errMsg);
            throw new IocException(errMsg);
        }
        rankStack.push(rankItem);
    }

    public boolean addIoclet(Ioclet ioclet) {
        RankItem rankItem = rankIndex.get(ioclet.getRank());
        if (rankItem == null) {
            // ignore ioclet  if it's rank is not registered in ranker
            if (log.isDebugEnabled()) {
                String errMsg = String.format("Unsupported rank '%s' for ioclet '%s'", ioclet.getRank(), ioclet.getClass().getName());
                log.debug(errMsg);
            }
            return false;
        }

        if (!rankItem.addIoclet(ioclet)) {
            throw new IocException(String.format("Duplicate ioclet '%s'", ioclet.getClass().getName()));
        }
        return true;
    }

    public Map<Key<?>, Factory<?>> toFactories() {
        Index index = new Index();
        // iterate ranks from TEST (max priority) to EAGER  (min priority)
        for (RankItem item : rankStack) {
            index.setCurrentRank(item.getRank());
            for (Ioclet ioclet : item.getIoclets()) {
                ioclet.addFactories(index);
            }
        }
        return index.getFactoriesMap();
    }


    public Deque<RankItem> getRankStack() {
        return rankStack;
    }

    static class Index implements Catalog {

        private final Map<Key<?>, Factory<?>> factoriesMap = new HashMap<>();
        private final Map<Key<?>, String> ranksMap = new HashMap<>();

        // Process state  (current values of rank, ket, etc)
        private final State state = new State();

        @Override
        public <T> void add(Factory<T> prodlet) {
            Factory oldItem = factoriesMap.put(state.key, prodlet);
            if (oldItem != null) {
                if (state.polyproducing) {
                    prodlet.setNext(oldItem);
                } else {
                    throw new IocException("Factory is overridden for key: " + state.key);
                }
            }
            ranksMap.put(state.key, state.rank);
        }

        @Override
        public <T> boolean accept(final Entry<T> entry) {
            final Key<T> key = entry.getKey();
            final boolean polyproducing = entry.isPolyproducing();

            // store state
            state.key = key;
            state.polyproducing = polyproducing;

            // if factory is not yet registered - it can be accepted
            if (!factoriesMap.containsKey(key)) {
                return true;
            }

            // Here is key is already banded to another factory.
            // And that factory can has a rank greater  or equal to current

            String rank = ranksMap.get(key);

            // If current rank is equal to that one and the current key is "polyproduce"
            // then current factory can be attached to key
            if (rank.equals(state.rank)) {
                if (polyproducing) {
                    return true;
                } else {
                    throw new IocException("Ambiguous factory for key: " + key);
                }
            }

            // here is known that the factory  has lo priority rank than the old one, so can not be replaced
            return false;
        }

        public void setCurrentRank(String rank) {
            state.rank = rank;
        }

        public Map<Key<?>, Factory<?>> getFactoriesMap() {
            return factoriesMap;
        }
    }


    public static final class RankItem {
        private final String rank;
        private final Set<Ioclet> ioclets = new HashSet<>();

        public RankItem(String rank) {
            this.rank = rank;
        }

        public boolean addIoclet(Ioclet ioclet) {
            return ioclets.add(ioclet);
        }

        public String getRank() {
            return rank;
        }

        public Set<Ioclet> getIoclets() {
            return ioclets;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RankItem that = (RankItem) o;
            return rank.equals(that.rank);
        }

        @Override
        public int hashCode() {
            return rank.hashCode();
        }

        @Override
        public String toString() {
            return "RankItem{ rank='" + rank + "'}";
        }
    }

    static final class State {
        protected Key<?> key;
        protected boolean polyproducing;
        protected String rank;

    }
}
