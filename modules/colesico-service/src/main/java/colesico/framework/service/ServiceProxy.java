/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package colesico.framework.service;

/**
 * Service base interface.
 * Automatically implemented by all classes marked with @Service annotation
 *
 * @author Vladlen Larionov
 */
public interface ServiceProxy {

    String SERVICE_CLASS_SUFFIX = "Service";
    String PROXY_CLASS_SUFFIX = "Proxy";
    String GET_SERVICE_ORIGIN_METHOD = "getServiceOrigin";

    /**
     * Returns service origin class (i.e. class on which this proxy is based)
     *
     * @return
     */
    Class<?> getServiceOrigin();

}
