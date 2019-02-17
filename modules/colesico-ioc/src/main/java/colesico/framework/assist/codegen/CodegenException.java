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

package colesico.framework.assist.codegen;

import colesico.framework.assist.codegen.model.ParserElement;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * An exception thrown while code generation in error case
 * @author Vladlen Larionov
 */
public class CodegenException extends RuntimeException {
    protected final Element element;
    protected final AnnotationMirror annotation;

    private CodegenException(String message, Throwable cause, Element e, AnnotationMirror a) {
        super(message, cause);
        this.element = e;
        this.annotation = a;
    }

    public Element getElement() {
        return element;
    }

    public AnnotationMirror getAnnotation() {
        return annotation;
    }

    public void print(ProcessingEnvironment processingEnv, Element defaultElement) {
        String message = getMessage();
        if (StringUtils.isEmpty(message)) {
            message = ExceptionUtils.getRootCauseMessage(this);
        }

        if (element != null && annotation != null) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element, annotation);
        } else if (element != null && annotation == null) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
        } else if (element == null && annotation != null) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, defaultElement, annotation);
        } else {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, defaultElement);
        }
    }

    public static Builder of() {
        return new Builder();
    }

    public static class Builder {
        protected String message;
        protected Throwable cause;
        protected Element element;
        protected AnnotationMirror annotation;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public Builder element(Element element) {
            this.element = element;
            return this;
        }

        public Builder element(ParserElement element) {
            this.element = element.unwrap();
            return this;
        }

        public Builder annotation(AnnotationMirror annotation) {
            this.annotation = annotation;
            return this;
        }

        public CodegenException build() {
            return new CodegenException(message, cause, element, annotation);
        }
    }
}
