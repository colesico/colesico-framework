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

import colesico.framework.assist.StrUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Constructor;

public class RecordKitFactory {

    public static final String KIT_IMPL_CLASS_SUFFIX = "Impl";

    private static <R, K extends RecordKitApi<R>> K getKit(String kitClassName, String view) {
        try {
            Class<K> kitClass = (Class<K>) Class.forName(kitClassName);
            Constructor<K> constructor = kitClass.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Record kit '" + kitClassName
                    + "' (view=" + view + ')'
                    + " instantiation error: " + ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public static <R, K extends RecordKitApi<R>> K getKit(Class<K> recordKitClass) {
        String kitClassName = recordKitClass.getName() + KIT_IMPL_CLASS_SUFFIX;
        return getKit(kitClassName, RecordView.DEFAULT_VIEW);
    }

    public static <R, K extends RecordKitApi<R>> K getKit(Class<K> recordKitClass, String view) {

        if (view == null || RecordView.DEFAULT_VIEW.equals(view)) {
            return getKit(recordKitClass);
        }

        String kitClassName = recordKitClass.getName() + StrUtils.firstCharToUpperCase(view) + KIT_IMPL_CLASS_SUFFIX;
        return getKit(kitClassName, view);
    }
}
