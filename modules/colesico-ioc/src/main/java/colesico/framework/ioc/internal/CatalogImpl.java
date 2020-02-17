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

import colesico.framework.ioc.IocException;
import colesico.framework.ioc.conditional.Condition;
import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.ioc.ioclet.Catalog;
import colesico.framework.ioc.ioclet.Factory;
import colesico.framework.ioc.key.Key;

import java.util.HashMap;
import java.util.Map;

public class CatalogImpl implements Catalog {

    protected final Map<Key<?>, CatalogEntry<?>> entriesMap = new HashMap();
    protected CatalogEntry curEntry;


    @Override
    public <T> boolean accept(Key<T> key, Condition condition, Substitution substitution, boolean polyproducing) {

        curEntry = new CatalogEntry(key, condition, substitution, polyproducing);

        // Check condition
        if ((condition != null) && !condition.isMet(null)) {
            return false;
        }

        CatalogEntry prevEntry = entriesMap.get(key);
        if (prevEntry == null) {
            curEntry.setAction(EntryAction.PUT);
            return true;
        }

        // Check substitution
        if (curEntry.getSubstitution() != null) {
            if (curEntry.isPolyproducing() != prevEntry.isPolyproducing()) {
                throw new IocException("Substitution polyproducing mismatch for key: " + key);
            }

            if (
                    prevEntry.getSubstitution() == null
                            || curEntry.getSubstitution().getRank() > prevEntry.getSubstitution().getRank()
            ) {
                curEntry.setAction(EntryAction.SUBSTITUTE);
                return true;
            }

            if (curEntry.getSubstitution().getRank() == prevEntry.getSubstitution().getRank()) {
                throw new IocException("Ambiguous substitution definition for key: " + key);
            }

            return false;
        }

        if (curEntry.isPolyproducing() && prevEntry.isPolyproducing()) {
            curEntry.setAction(EntryAction.APPEND);
            return true;
        }

        return false;
    }

    @Override
    public <T> void add(Factory<T> factory) {
        curEntry.setFactory(factory);
        CatalogEntry<?> prevEntry = entriesMap.put(curEntry.getKey(), curEntry);
        if (prevEntry != null) {
            if (prevEntry.isPolyproducing()) {
                curEntry.getFactory().setNext(prevEntry.getFactory());
            } else {
                throw new IocException("Factory has overridden for key: " + curEntry.getKey());
            }
        }
    }

    public Map<Key<?>, Factory<?>> getFactories() {
        return null;
    }


}
