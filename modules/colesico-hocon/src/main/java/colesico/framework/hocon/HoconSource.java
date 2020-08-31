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

package colesico.framework.hocon;

import colesico.framework.assist.StrUtils;
import colesico.framework.config.ConfigSource;
import colesico.framework.config.UseFileSource;
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

import com.typesafe.config.*;

import static colesico.framework.config.UseFileSource.*;

/**
 * Hocon file based configuration source.
 * Lookup hocon file in external directory specified by "directory" config param.
 * If not found trying to read file from classpath specified by "classpath" parameter.
 */
@Singleton
public class HoconSource implements ConfigSource {

    /**
     * Hocon value path prefix
     */
    public static final String PREFIX_OPTION = UseFileSource.PREFIX_OPTION;

    protected static final Logger logger = LoggerFactory.getLogger(HoconSource.class);

    @Override
    public Connection connect(Map<String, String> params) {
        String fileName = params.getOrDefault(FILE_OPTION, "application.conf");

        final String directory = params.getOrDefault(DIRECTORY_OPTION, "./config");
        String fullPath = StrUtils.concatPath(directory, fileName, "/");

        final File directoryFile = new File(fullPath);
        Config config;
        if (directoryFile.exists()) {
            logger.debug("Read configuration from file: " + fullPath);
            try {
                config = ConfigFactory.parseFile(directoryFile);
                return createConnection(config, params);
            } catch (Exception e) {
                String errorMsg = "Error reading config from file: " + fullPath;
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg, e);
            }
        }

        final String classpath = params.getOrDefault(CLASSPATH_OPTION, "META-INF");
        fullPath = StrUtils.concatPath(classpath, fileName, "/");

        try {
            config = ConfigFactory.parseResources(fullPath);
            return createConnection(config, params);
        } catch (Exception e) {
            String errorMsg = "Error reading config from resource: " + fullPath;
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    protected ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader;
    }

    protected Connection createConnection(Config config, Map<String, String> params) {
        return new ConnectionImpl(config, params.get(PREFIX_OPTION));
    }

    protected static class ConnectionImpl implements Connection {

        private final Config config;
        private final String prefix;

        public ConnectionImpl(Config config, String prefix) {
            this.config = config;
            this.prefix = prefix;
        }

        @Override
        public <T> T getValue(Type valueType) {
            //T result = config.getAnyRef(valueType, prefix);
            //return result;
            //TODO:
            return null;
        }

        @Override
        public void close() {
            // nop
        }


    }

}
