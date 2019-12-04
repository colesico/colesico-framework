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

package colesico.framework.hikaricp;

import colesico.framework.assist.StrUtils;
import com.zaxxer.hikari.HikariConfig;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties file based HikariCP config.
 *
 * Usage:
 * <pre>
 *   class AClass {
 *      public AClass( @Classed(HikariProperties.class) DataSource dataSource){...}
 *   }
 * </pre>
 */
public class HikariProperties extends HikariConfigPrototype {

    protected final Logger logger = LoggerFactory.getLogger(HikariProperties.class);

    /**
     * Properties file name.
     * Default: hikari.properties
     */
    protected String getFileName() {
        return "hikari.properties";
    }

    /**
     * Properties file  directory
     * Default: ./config
     */
    protected String getDirectory() {
        return "./config";
    }

    /**
     * Properties file classpath directory
     * Default: META-INF
     */
    protected String getClasspath() {
        return "META-INF";
    }

    @Override
    public final HikariConfig getHikariConfig() {

        final Properties props = new Properties();
        String fullPath = StrUtils.concatPath(getDirectory(), getFileName(), "/");
        final File directoryFile = new File(fullPath);
        if (directoryFile.exists()) {
            logger.info("Read HikariCP configuration from file: " + fullPath);
            try (FileInputStream is = new FileInputStream(fullPath)) {
                props.load(is);
                return new HikariConfig(props);
            } catch (Exception e) {
                String errorMsg = "Error reading HicariCP config from file: " + fullPath + "; " + ExceptionUtils.getRootCauseMessage(e);
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg, e);
            }
        }

        fullPath = StrUtils.concatPath(getClasspath(), getFileName(), "/");

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = loader.getResourceAsStream(fullPath)) {
            if (is != null) {
                logger.info("Read HikariCP configuration from resource: " + fullPath);
                props.load(is);
                return new HikariConfig(props);
            } else {
                throw new RuntimeException("HicariCP config file not found: " + fullPath);
            }
        } catch (Exception e) {
            String errorMsg = "Error reading HicariCP config  from resource: " + fullPath + "; " + ExceptionUtils.getRootCauseMessage(e);
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg, e);
        }

    }

    @Override
    public String toString() {
        return "HikariProperties{" + getDirectory() + "/" + getFileName()
            + " | " + getClasspath() + "/" + getFileName() + "}";
    }
}

