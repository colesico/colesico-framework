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

import colesico.framework.ioc.AmbiguousDependencyException;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.IocException;
import colesico.framework.ioc.conditional.Condition;
import colesico.framework.ioc.conditional.ConditionContext;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.ioclet.Catalog;
import colesico.framework.ioc.ioclet.Factory;
import colesico.framework.ioc.key.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CatalogImpl implements Catalog {

    private static final Logger log = LoggerFactory.getLogger(Catalog.class);

    protected final Map<Key<?>, CatalogEntry<?>> entriesMap = new HashMap();
    protected CatalogEntry curEntry;
    protected final ConditionContext conditionContext;

    public CatalogImpl(ConditionContext conditionContext) {
        this.conditionContext = conditionContext;
    }

    @Override
    public <T> boolean accept(Key<T> key, Condition condition, Substitution substitution, boolean polyproducing) {

        curEntry = new CatalogEntry(key, condition, substitution, polyproducing);

        // Check condition
        if ((condition != null) && !condition.isMet(conditionContext)) {
            log.debug("Condition not met for key: " + key);
            return false;
        }

        CatalogEntry prevEntry = entriesMap.get(key);
        if (prevEntry == null) {
            curEntry.setAction(EntryAction.PUT);
            return true;
        }

        // Check substitution
        if (curEntry.getSubstitution() != null || prevEntry.getSubstitution() != null) {

            if (curEntry.isPolyproducing() != prevEntry.isPolyproducing()) {
                throw new IocException("Substitution polyproducing mismatch for key: " + key);
            }

            if (curEntry.getSubstitution() != null && prevEntry.getSubstitution() != null) {

                if (curEntry.getSubstitution().getRank() > prevEntry.getSubstitution().getRank()) {
                    curEntry.setAction(EntryAction.SUBSTITUTE);
                    return true;
                }

                if (curEntry.getSubstitution().getRank() < prevEntry.getSubstitution().getRank()) {
                    curEntry.setAction(EntryAction.NONE);
                    return true;
                }

                throw new AmbiguousDependencyException("Ambiguous substitution definition for key: " + key);
            }

            if (curEntry.getSubstitution() != null && prevEntry.getSubstitution() == null) {
                curEntry.setAction(EntryAction.SUBSTITUTE);
                return true;
            }

            // curEntry.getSubstitution() == null && prevEntry.getSubstitution() != null
            curEntry.setAction(EntryAction.NONE);
            return false;
        }

        if (curEntry.isPolyproducing() && prevEntry.isPolyproducing()) {
            curEntry.setAction(EntryAction.APPEND);
            return true;
        }

        throw new AmbiguousDependencyException("Ambiguous factory for key: " + key + ";");

    }

    @Override
    public <T> void add(Factory<T> factory) {
        curEntry.setFactory(factory);
        switch (curEntry.getAction()) {
            case NONE:
                throw new IocException("Cataloging is not permitted");
            case PUT:
            case SUBSTITUTE:
                entriesMap.put(curEntry.getKey(), curEntry);
                return;
            case APPEND:
                CatalogEntry<?> prevEntry = entriesMap.put(curEntry.getKey(), curEntry);
                curEntry.getFactory().setNext(prevEntry.getFactory());
                return;
            default:
                throw new IocException("Unsupported catalog action: " + curEntry.getAction());
        }
    }

    public Map<Key<?>, Factory<?>> getFactories() {
        Map<Key<?>, Factory<?>> result = new HashMap<>();
        for (CatalogEntry entry : entriesMap.values()) {
            result.put(entry.getKey(), entry.getFactory());
        }
        return result;
    }


}
