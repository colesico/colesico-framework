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

    public static final String KIT_CLASS_SUFFIX = "RecordKit";

    private static <R, K extends RecordKit<R>> K getKit(String kitClassName, Class<R> recordClass, String view) {
        try {
            Class<K> kitClass = (Class<K>) Class.forName(kitClassName);
            Constructor<K> constructor = kitClass.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Record kit '" + kitClassName + "' for record class '" + recordClass.getName()
                + "' (view=" + view + ')'
                + " instantiation error: " + ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public static <R, K extends RecordKit<R>> K getKit(Class<R> recordClass) {
        String kitClassName = recordClass.getName() + KIT_CLASS_SUFFIX;
        return getKit(kitClassName, recordClass, RecordView.DEFAULT_VIEW);
    }

    public static <R, K extends RecordKit<R>> K getKit(Class<R> recordClass, String view) {

        if (view == null || RecordView.DEFAULT_VIEW.equals(view)) {
            return getKit(recordClass);
        }
        
        String kitClassName = recordClass.getName() + StrUtils.firstCharToUpperCase(view) + KIT_CLASS_SUFFIX;
        return getKit(kitClassName, recordClass, view);
    }
}
