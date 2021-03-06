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

package colesico.framework.ioc.production;

import colesico.framework.ioc.conditional.Condition;
import colesico.framework.ioc.scope.Unscoped;

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

    String POST_PRODUCE_METHOD = "postProduce";
    String POST_CONSTRUCT_METHOD = "postConstruct";
    String CLASSED_METHOD = "classed";
    String NAMED_METHOD = "named";
    String POLYPRODUCE_METHOD = "polyproduce";
    String VALUE_METHOD = "value";


    /**
     * Class of instance to be produced
     */
    Class<?> value();

    /**
     * Producing condition
     *
     * @see colesico.framework.ioc.conditional.Requires
     * @see Condition
     */
    Class<? extends Condition> requires() default Condition.class;

    /**
     * Analogue of the @Polyproduce annotation
     *
     * @see Polyproduce
     */
    boolean polyproduce() default false;

    /**
     * Analogue of the @Named annotation
     *
     * @see javax.inject.Named
     */
    String named() default "";

    /**
     * Analogue of th @Classed annotation
     *
     * @see Classed
     */
    Class<?> classed() default Class.class;

    /**
     * Whether or not to invoke post produce listener after instance been produced (but before instance @PostConstruct).
     * This  can be used to handle just created instance before it will be returned from IoC container
     */
    boolean postProduce() default false;

    /**
     * Whether or not to call instance @PostConstruct listeners
     */
    boolean postConstruct() default true;

    /**
     * Instance class itself, superclasses or interfaces with which this instance will be associated.
     * This instance will acts as an implementation for these classes or interfaces.
     * @see KeyType
     */
    Class<?>[] keyType() default {};

}
