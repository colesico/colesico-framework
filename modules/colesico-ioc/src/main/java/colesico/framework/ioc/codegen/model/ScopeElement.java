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

package colesico.framework.ioc.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;

/**
 * @author Vladlen Larionov
 */
public class ScopeElement {
    private final ClassType scopeClass;
    private final ScopeKind kind;

    public ScopeElement(ClassType scopeClass, ScopeKind kind) {
        this.scopeClass = scopeClass;
        this.kind = kind;
    }

    public ClassType getScopeClass() {
        return scopeClass;
    }

    public ScopeKind getKind() {
        return kind;
    }

    @Override
    public String toString() {
        return "ScopeElement{" +
                "scopeClass=" + scopeClass +
                ", kind=" + kind +
                '}';
    }

    public enum ScopeKind {
        SINGLETON, UNSCOPED, CUSTOM
    }
}
