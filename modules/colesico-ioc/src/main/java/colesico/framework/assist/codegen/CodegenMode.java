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

package colesico.framework.assist.codegen;

import org.apache.commons.lang3.StringUtils;
/*

  Usage in maven compiler plugin:
  <compilerArgs>
    <!-- Code generation model: default|optimized -->
    <arg>-Acolesico.framework.codegen=${codegen-mode}</arg>
  </compilerArgs>

 */
public enum CodegenMode {

    DEFAULT("default"),
    OPTIMIZED("optimized");

    private final String key;

    CodegenMode(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public boolean isDefault() {
        return "default".equals(key);
    }

    public boolean isOptimized() {
        return "optimized".equals(key);
    }

    public static CodegenMode fromKey(String key) {
        // Default codegen mode is for production
        if (StringUtils.isBlank(key)) {
            return CodegenMode.DEFAULT;
        }

        // find out specified mode
        for (CodegenMode mode : CodegenMode.values()) {
            if (mode.getKey().equals(key)) {
                return mode;
            }
        }

        throw new RuntimeException("Unsupported code generation mode:" + key);
    }
}
