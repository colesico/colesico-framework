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

package colesico.framework.rpc.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.modulator.TeleModulatorContext;

import java.util.ArrayList;
import java.util.List;

public class RpcModulatorContext implements TeleModulatorContext {

    private final ServiceElement serviceElement;

    private final List<ClassType> originRpcInterfaces;

    private List<RpcTeleMethodElement> teleMethods = new ArrayList<>();

    public RpcModulatorContext(ServiceElement serviceElement, List<ClassType> originRpcInterfaces) {
        this.serviceElement = serviceElement;
        this.originRpcInterfaces = originRpcInterfaces;
    }

    @Override
    public void registTeleMethod(TeleMethodElement teleMethodElement) {
        RpcTeleMethodElement rpcTme = new RpcTeleMethodElement(teleMethodElement);
        teleMethods.add(rpcTme);
    }

    public List<RpcTeleMethodElement> getTeleMethods() {
        return teleMethods;
    }

    public ServiceElement getServiceElement() {
        return serviceElement;
    }

    public List<ClassType> getOriginRpcInterfaces() {
        return originRpcInterfaces;
    }
}
