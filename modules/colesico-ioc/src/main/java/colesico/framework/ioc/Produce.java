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

import java.lang.annotation.*;

/**
 * Declares the instance default producing.
 *
 * @author Vladlen Larionov
 * @see Unscoped
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
@Repeatable(Produces.class)
public @interface Produce {
    /**
     * Class of instance to be produced
     *
     * @return Class of instance to be produced
     */
    Class<?> value();

    /**
     * Analogue of the @Polyproduce annotation
     *
     * @return
     * @see Polyproduce
     */
    boolean polyproduce() default false;

    /**
     * Analogue of the @Named annotation
     *
     * @return name
     * @see javax.inject.Named
     */
    String named() default "";

    /**
     * Analogue of th @Classed annotation
     *
     * @return
     * @see Classed
     */
    Class<?> classed() default Class.class;
}
