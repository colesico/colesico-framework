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

package colesico.framework.example.rpc;

import colesico.framework.example.rpc.api.NameBean;
import colesico.framework.example.rpc.api.DataBean;
import colesico.framework.example.rpc.api.HelloServiceRemote;
import colesico.framework.service.Service;

@Service
public class HelloService implements HelloServiceRemote {

    @Override
    public String getMessage(Integer id, DataBean dataBean, NameBean nb) {
        return  "ID:" + id +" DB:" +dataBean.toString()
                + " NB:" + nb.toString();
    }

    @Override
    public DataBean getDataBean() {
        return new DataBean(HELLO_MESSAGE);
    }

}
