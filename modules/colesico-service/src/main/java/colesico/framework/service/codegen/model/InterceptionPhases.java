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
package colesico.framework.service.codegen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Interception phases in execution order.
 * Higher in the list means more "outer" (wraps subsequent phases).
 */
public final class InterceptionPhases {

    /**
     * Common purpose initial phase
     */
    public static final String BOOTSTRAP = "BOOTSTRAP";

    /**
     * Error handling and transformation
     */
    public static final String ERRORS = "ERRORS";

    /**
     * Global logging/tracing of the request execution
     */
    public static final String LOGGING = "LOGGING";

    /**
     * SLA, Latency, Throughput metrics
     */
    public static final String METRICS = "METRICS";

    /**
     * Request frequency control
     */
    public static final String RATE_LIMITING = "RATE_LIMITING";

    /**
     * Permission and access control
     */
    public static final String AUTHORIZATION = "AUTHORIZATION";

    /**
     * Input parameters validation (prevents unnecessary cache lookups)
     */
    public static final String VALIDATION = "VALIDATION";

    /**
     * Method result caching
     */
    public static final String CACHING = "CACHING";

    /**
     * Fault tolerance: circuit breaker and retries
     */
    public static final String CIRCUIT_BREAKER = "CIRCUIT_BREAKER";


    /**
     * Database transaction management
     */
    public static final String TRANSACTION = "TRANSACTION";

    /**
     * Resource management (DB connections, handles, sessions)
     */
    public static final String RESOURCES = "RESOURCES";

    /**
     * Common purpose phase just before target method invocation
     */
    public static final String PREPROCESS = "PREPROCESS";

    /**
     * Common purpose phase just after target method invocation (inside transaction)
     */
    public static final String POSTPROCESS = "POSTPROCESS";

    /**
     * Common purpose final phase
     */
    public static final String TEARDOWN = "TEARDOWN";

    private final List<String> phaseOrder;

    public InterceptionPhases() {
        phaseOrder = new ArrayList<>();

        // 1. Infrastructure
        phaseOrder.add(BOOTSTRAP);
        phaseOrder.add(ERRORS);
        phaseOrder.add(LOGGING);
        phaseOrder.add(METRICS);
        phaseOrder.add(RATE_LIMITING);

        // 2. Security
        phaseOrder.add(AUTHORIZATION);

        // 3. Traffic control & Validation
        phaseOrder.add(VALIDATION);
        phaseOrder.add(CACHING);
        phaseOrder.add(CIRCUIT_BREAKER);

        // 5. Data & Context
        phaseOrder.add(TRANSACTION);
        phaseOrder.add(RESOURCES);

        // 6. Main action wrappers
        phaseOrder.add(PREPROCESS);
        phaseOrder.add(POSTPROCESS);

        // 7. Final action
        phaseOrder.add(TEARDOWN);
    }

    public List<String> phaseOrder() {
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

    public void addPhaseFirst(String newPhase) {
        if (checkPhaseExists(newPhase)) {
            throw new RuntimeException("Interception phase already exists: " + newPhase);
        }
        phaseOrder.add(0, newPhase);
    }

    public void addPhaseLast(String newPhase) {
        if (checkPhaseExists(newPhase)) {
            throw new RuntimeException("Interception phase already exists: " + newPhase);
        }
        phaseOrder.add(newPhase);
    }

    private void checkAddPhase(String existingPhase, String newPhase) {
        if (checkPhaseExists(newPhase)) {
            throw new RuntimeException("Interception phase already exists: " + newPhase);
        }
        if (!checkPhaseExists(existingPhase)) {
            throw new RuntimeException("Interception doesn't  exists: " + existingPhase);
        }
    }

}
