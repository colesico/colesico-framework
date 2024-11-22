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

package colesico.framework.jdbirec;

import java.lang.annotation.*;

/**
 * Record kit configuration.
 * <p>
 * Define this annotation on record kit interface
 * </p>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RecordKit {

    /**
     * Master table name for record
     */
    String table() default "";

    /**
     * Master table alias to use in sql queries
     */
    String tableAlias() default "";

    /**
     * Record views definition
     */
    RecordView[] views() default {};

    /**
     * Interpret the composition as join records (select from table join...)
     */
    Class<? extends RecordKitApi>[] join() default {};

    /**
     * Base class to be extended by this record kit generated implementation
     */
    Class<? extends RecordKitApi> superclass() default AbstractRecordKitApi.class;

}
