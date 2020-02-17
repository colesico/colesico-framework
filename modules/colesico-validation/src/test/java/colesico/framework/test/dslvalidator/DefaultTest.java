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

package colesico.framework.test.dslvalidator;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.ioc.conditional.TestCondition;
import colesico.framework.validation.ValidationIssue;
import colesico.framework.validation.Validator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.inject.Provider;

public class DefaultTest {

    private Ioc ioc;
    private MyDataBean dataBean;

    @BeforeClass
    public void init() {
        TestCondition.enable();
        ioc = IocBuilder.create().build();
        dataBean = new MyDataBean(10L,"AName","AValue");
    }

    @Test
    public void test1() {
        Provider<MyValidatorBuilder> vbProv = ioc.provider(MyValidatorBuilder.class);
        Validator validatorGroup = vbProv.get().buildGroup();
        ValidationIssue vi = validatorGroup.validate(dataBean);
        //System.out.println("Group");
        //System.out.println(vi);

        Validator validatorSubj = vbProv.get().buildSubject();
        ValidationIssue vi2 = validatorSubj.validate(dataBean);
        //System.out.println("Subj");
        //System.out.println(vi2);

        ValidationIssue vi3 = validatorSubj.validate(null);
        //System.out.println("SubjRequired ");
        //System.out.println(vi3);
    }
}
