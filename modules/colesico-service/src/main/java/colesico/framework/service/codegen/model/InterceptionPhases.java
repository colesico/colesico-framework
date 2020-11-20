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
package colesico.framework.service.codegen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Interception phases
 */
public final class InterceptionPhases {
    /**
     * Common purpose initial phase
     */
    public static final String BOOTSTRAP = "BOOTSTRAP";

    /**
     * Logging phase
     */
    public static final String LOGGING = "LOGGING";

    /**
     * Transaction control phase
     */
    public static final String TRANSACTION = "TRANSACTION";

    /**
     * Authorization control phase
     */
    public static final String AUTHORIZATION = "AUTHORIZATION";

    /**
     * Parameters validation phase
     */
    public static final String VALIDATION = "VALIDATION";

    /**
     * Method invocation result caching phase
     */
    public static final String CACHING = "CACHING";

    /**
     * Resource initialization phase  (db connection)
     */
    public static final String RESOURCES = "RESOURCES";

    /**
     * Common purpose phase just before target method been invoked
     */
    public static final String PREPROCESS = "PREPROCESS";

    private List<String> phaseOrder;

    public InterceptionPhases() {
        // Set default invocation phases
        phaseOrder = new ArrayList<>();
        phaseOrder.add(BOOTSTRAP);
        phaseOrder.add(LOGGING);
        phaseOrder.add(TRANSACTION);
        phaseOrder.add(AUTHORIZATION);
        phaseOrder.add(VALIDATION);
        phaseOrder.add(CACHING);
        phaseOrder.add(RESOURCES);
        phaseOrder.add(PREPROCESS);
    }

    public List<String> getPhaseOrder() {
        return Collections.unmodifiableList(phaseOrder);
    }

    public boolean checkPhaseExists(String phase) {
        return phaseOrder.indexOf(phase) >= 0;
    }

    public void addPhaseAfter(String existingPhase, String newPhase) {
        checkAddPhase(existingPhase, newPhase);
        int phaseIndex = phaseOrder.indexOf(existingPhase) + 1;
        phaseOrder.add(phaseIndex, newPhase);
    }

    public void addPhaseBefore(String existingPhase, String newPhase) {
        checkAddPhase(existingPhase, newPhase);
        int phaseIndex = phaseOrder.indexOf(existingPhase);
        phaseOrder.add(phaseIndex, newPhase);
    }

    private void checkAddPhase(String exisingPhase, String newPhase) {
        if (checkPhaseExists(newPhase)) {
            throw new RuntimeException("Interception phase already exists: " + newPhase);
        }
        if (!checkPhaseExists(exisingPhase)) {
            throw new RuntimeException("Interception doesn't  exists: " + exisingPhase);
        }
    }

}
