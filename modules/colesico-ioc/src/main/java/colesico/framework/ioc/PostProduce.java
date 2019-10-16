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

package colesico.framework.ioc;

import colesico.framework.ioc.ioclet.PPLKey;

import java.lang.annotation.*;

/**
 * Post produce listener declaration.
 * This annotation should be allied to producer method to declare post produce listener.
 * The post produce listener invoked by IoC container to handle just produced instance.
 * Post produce listener invoked before @PostConstruct listener.
 *
 * @author Vladlen Larionov
 * @see PPLKey
 * @see Produce
 * @see Producer
 * @see PostConstruct
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
@Documented
public @interface PostProduce {
    /**
     * Reference to named value of producing method or @Produce declaration
     *
     * @return
     */
    String withNamed() default "";

    /**
     * Reference to classed value of producing method or @Produce declaration
     *
     * @return
     */
    Class<?> withClassed() default Class.class;
}
