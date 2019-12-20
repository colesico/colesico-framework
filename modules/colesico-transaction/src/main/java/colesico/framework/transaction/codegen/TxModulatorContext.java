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

package colesico.framework.transaction.codegen;

import java.util.HashMap;
import java.util.Map;

public class TxModulatorContext {

    private Map<String, Integer> shellsIdx = new HashMap<>();

    private int idxCounter = 0;

    public Integer getShellIndex(String named) {
        Integer idx = shellsIdx.get(named);
        if (idx == null) {
            idx = idxCounter++;
            shellsIdx.put(named, idx);
        }

        return idx;
    }

}
