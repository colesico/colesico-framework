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

import colesico.framework.assist.codegen.model.ParameterElement;
import com.squareup.javapoet.CodeBlock;

import java.util.HashMap;
import java.util.Map;

/**
 * Tele-method parameter
 */
public final class TeleParamElement {

    /**
     * Parent tele-method ref
     */
    protected TeleMethodElement parentTeleMethod;

    /**
     * Origin method param ref
     */
    protected final ParameterElement originParam;

    /**
     * Param reading context code
     */
    private CodeBlock readingContextCode;

    /**
     * Parameter index
     */
    protected final Integer paramIndex;

    /**
     * Custom purpose props
     */
    private Map<Class<?>, Object> properties = new HashMap<>();

    public TeleParamElement(ParameterElement originParam, Integer paramIndex) {
        this.originParam = originParam;
        this.paramIndex = paramIndex;
    }

    public <C> C getProperty(Class<C> propertyClass) {
        return (C) properties.get(propertyClass);
    }

    public void setProperty(Class<?> propertyClass, Object property) {
        properties.put(propertyClass, property);
    }

    public TeleMethodElement getParentTeleMethod() {
        return parentTeleMethod;
    }

    public void setParentTeleMethod(TeleMethodElement parentTeleMethod) {
        this.parentTeleMethod = parentTeleMethod;
    }

    public ParameterElement getOriginParam() {
        return originParam;
    }

    public CodeBlock getReadingContextCode() {
        return readingContextCode;
    }

    public void setReadingContextCode(CodeBlock readingContextCode) {
        this.readingContextCode = readingContextCode;
    }

    public Integer getParamIndex() {
        return paramIndex;
    }

    @Override
    public String toString() {
        return "TeleParamElement{" +
                "originParam=" + originParam +
                ", paramIndex=" + paramIndex +
                '}';
    }
}
