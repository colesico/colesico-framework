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

package colesico.framework.ioc;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class InstanceProducingException extends RuntimeException {
    private Class<?> target;

    public InstanceProducingException(Throwable cause, Class<?> target) {
        super(cause);
        this.target = target;
    }

    @Override
    public String getMessage() {
        String errMsg = "An exception occurred while producing instance of " + target.getName();
        if (getCause() instanceof StackOverflowError) {
            errMsg = errMsg + "; Possible the cyclic dependency";
        }
        errMsg = errMsg + "; Root message: " + ExceptionUtils.getRootCauseMessage(this);
        return errMsg;
    }
}
