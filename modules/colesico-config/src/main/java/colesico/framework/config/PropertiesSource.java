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

package colesico.framework.config;

import colesico.framework.assist.StrUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

/**
 * Properties file based configuration source.
 * Lookup properties file in external directory specified by "directory" config param.
 * If not found trying to read file from classpath specified by "classpath" parameter.
 *
 * @see UseSource
 * @see FromSource
 */
@Singleton
public class PropertiesSource implements ConfigSource {

    /**
     * Properties file  directory
     * Default: ./config
     */
    public static final String DIRECTORY = "directory";

    /**
     * Properties file classpath directory
     * Default: META-INF
     */
    public static final String CLASSPATH = "classpath";

    /**
     * Properties file name.
     * Default: application.properties
     */
    public static final String FILE = "fileName";

    /**
     * Properties name prefix
     */
    public static final String PREFIX = "prefix";

    protected static final Logger logger = LoggerFactory.getLogger(PropertiesSource.class);
    protected static final String DEFAULT_PROPERTIES_FILE = "application.properties";

    @Override
    public Connection connect(Map<String, String> params) {
        String fileName = params.get(DEFAULT_PARAM);
        if (fileName == null) {
            fileName = params.getOrDefault(FILE, DEFAULT_PROPERTIES_FILE);
        }

        final String directory = params.getOrDefault(DIRECTORY, "./config");
        String fullPath = StrUtils.concatPath(directory, fileName, "/");

        final Properties props = new Properties();
        final File directoryFile = new File(fullPath);
        if (directoryFile.exists()) {
            logger.debug("Read configuration from file: " + fullPath);
            try (FileInputStream is = new FileInputStream(fullPath)) {
                props.load(is);
                return createConnection(props, params);
            } catch (Exception e) {
                String errorMsg = "Error reading config from file: " + fullPath;
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg, e);
            }
        }

        final String classpath = params.getOrDefault(CLASSPATH, "META-INF");
        fullPath = StrUtils.concatPath(classpath, fileName, "/");

        try (InputStream is = getClassLoader().getResourceAsStream(fullPath)) {
            if (is != null) {
                props.load(is);
                return createConnection(props, params);
            } else {
                throw new RuntimeException("File not found: " + fullPath);
            }
        } catch (Exception e) {
            String errorMsg = "Error reading config from  resource: " + fullPath;
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    protected ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader;
    }

    protected Connection createConnection(Properties properties, Map<String, String> params) {
        return new ConnectionImpl(properties, params.get(PREFIX));
    }

    protected static class ConnectionImpl implements Connection {

        private final Properties properties;
        private final String prefix;

        public ConnectionImpl(Properties properties, String prefix) {
            this.properties = properties;
            this.prefix = prefix;
        }

        @Override
        public <T> T getValue(Type valueType) {
            T result = buildComposition(valueType, prefix);
            return result;
        }

        @Override
        public void close() {
            // nop
        }

        protected <T> Function<String, T> getValueConverter(Type valueType) {
            if (valueType == String.class) {
                return v -> (T) v;
            }
            if (valueType == Long.class) {
                return v -> (T) Long.valueOf(v);
            }
            if (valueType == Integer.class) {
                return v -> (T) Integer.valueOf(v);
            }
            if (valueType == Short.class) {
                return v -> (T) Short.valueOf(v);
            }
            if (valueType == Byte.class) {
                return v -> (T) Byte.valueOf(v);
            }
            if (valueType == Boolean.class) {
                return v -> (T) Boolean.valueOf(v);
            }
            if (valueType == Double.class) {
                return v -> (T) Double.valueOf(v);
            }
            if (valueType == Float.class) {
                return v -> (T) Float.valueOf(v);
            }
            if (valueType == Character.class) {
                return v -> (T) Character.valueOf(v.charAt(0));
            }
            return null;
        }

        protected <T> T buildComposition(Type compositionType, String compositionPath) {
            try {
                Class<T> compositionClass = (Class<T>) compositionType;
                T compositionInstance = compositionClass.getDeclaredConstructor().newInstance();
                List<Method> setters = getSetters(compositionClass);
                for (Method setter : setters) {
                    String valueKey = toKey(compositionPath, getFieldName(setter));
                    Type valueType = setter.getParameterTypes()[0];
                    T value = null;
                    Function<String, T> valueConverter = getValueConverter(valueType);
                    if (valueConverter != null) {
                        String rawValue = properties.getProperty(valueKey);
                        if (rawValue != null) {
                            value = valueConverter.apply(rawValue);
                        }
                    } else {
                        value = buildComposition(valueType, valueKey);
                    }
                    setter.invoke(compositionInstance, value);
                }
                return compositionInstance;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        protected String toKey(String prefix, String path) {
            return (prefix != null ? prefix + '.' : "") + path;
        }

        protected List<Method> getSetters(Class clazz) {
            List<Method> result = new ArrayList<>();
            Class<?> currentClazz = clazz;
            while (currentClazz.getSuperclass() != null) { // we don't want to process Object.class
                for (Method method : currentClazz.getDeclaredMethods()) {
                    if (Modifier.isPublic(method.getModifiers())
                            && method.getParameterTypes().length == 1
                            && method.getReturnType() == void.class
                            && (method.getName().startsWith("set"))
                    ) {
                        method.setAccessible(true);
                        result.add(method);
                    }
                }
                currentClazz = currentClazz.getSuperclass();
            }
            return result;
        }

        protected String getFieldName(Method setter) {
            return StrUtils.firstCharToLowerCase(StringUtils.substring(setter.getName(), 3));
        }

    }

}
