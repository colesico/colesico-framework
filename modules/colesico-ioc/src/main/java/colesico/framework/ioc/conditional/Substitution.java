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

package colesico.framework.ioc.conditional;

/**
 * Substitution type.
 * Higher ranks has precedence over low to perform substitution
 */
public enum Substitution {

    /**
     * For dummy instance production.
     * The dummy instance can be replaced with regular instances  production  or higher rank.
     */
    DUMMY(-1),

    /**
     * Regular instance production rank.
     * This is the default rank for production where no {@link Substitute} annotation is specified.
     */
    REGULAR(0),

    /**
     * For replace the regular (or dummy) instances  production.
     * This is default rank for {@link Substitute} annotation
     */
    DEFAULT(1),

    /**
     * For plugins/extensions that replaces regular functionality
     */
    EXTENSION(2),

    /**
     * Instance production for testing purposes
     */
    TEST(3);

    /**
     * Substitution priority. 1 - minimum ; 3 - maximum
     */
    private final int rank;

    Substitution(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}
