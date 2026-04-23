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

package colesico.framework.ioc.internal;

import colesico.framework.ioc.AmbiguousDependencyException;
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

        // for older version ioclets compatibility
        if (substitution == null) {
            substitution = Substitution.REGULAR;
        }

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

        // Check polyproducing
        if (curEntry.isPolyproducing() != prevEntry.isPolyproducing()) {
            throw new IocException("Polyproducing mismatch for key: " + key);
        }

        // Check substitution
        if (curEntry.substitution().rank() > prevEntry.substitution().rank()) {
            curEntry.setAction(EntryAction.SUBSTITUTE);
            return true;
        }

        if (curEntry.substitution().rank() < prevEntry.substitution().rank()) {
            curEntry.setAction(EntryAction.NONE);
            return false;
        }

        // curEntry.getSubstitution().getRank() == prevEntry.getSubstitution().getRank()

        // Check polyproducing

        if (curEntry.isPolyproducing() && prevEntry.isPolyproducing()) {
            curEntry.setAction(EntryAction.APPEND);
            return true;
        }

        throw new AmbiguousDependencyException("Ambiguous factory for key: " + key + ";");

    }

    @Override
    public <T> void add(Factory<T> factory) {
        curEntry.setFactory(factory);
        switch (curEntry.action()) {
            case NONE:
                throw new IocException("Cataloging is not permitted");
            case PUT:
            case SUBSTITUTE:
                entriesMap.put(curEntry.key(), curEntry);
                return;
            case APPEND:
                CatalogEntry<?> prevEntry = entriesMap.put(curEntry.key(), curEntry);
                curEntry.gfactory().setNext(prevEntry.gfactory());
                return;
            default:
                throw new IocException("Unsupported catalog action: " + curEntry.action());
        }
    }

    public Map<Key<?>, Factory<?>> factories() {
        Map<Key<?>, Factory<?>> result = new HashMap<>();
        for (CatalogEntry entry : entriesMap.values()) {
            result.put(entry.key(), entry.gfactory());
        }
        return result;
    }


}
