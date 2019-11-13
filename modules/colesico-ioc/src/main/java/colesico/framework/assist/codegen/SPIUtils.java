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

package colesico.framework.assist.codegen;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SPIUtils {

    public static final String SPI_FILES_PATH = "META-INF/services/";

    static final Logger logger = LoggerFactory.getLogger(SPIUtils.class);

    @SuppressWarnings("unchecked")
    public static Set<String> readSPIFile(InputStream input) {
        Set<String> srvClasses = new HashSet();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                int commentStart = line.indexOf('#');
                if (commentStart >= 0) {
                    line = line.substring(0, commentStart);
                }

                line = StringUtils.trim(line);
                if (StringUtils.isNoneBlank(line)) {
                    srvClasses.add(line);
                }
            }
            return srvClasses;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeSPIFile(OutputStream output, Collection<String> srvClasses) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
            Iterator it = srvClasses.iterator();

            while (it.hasNext()) {
                String service = (String) it.next();
                writer.write(service);
                writer.newLine();
            }

            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void addService(String srvInterface, Set<String> srvClasses, ProcessingEnvironment processingEnv) {
        logger.debug("Generate SPI file for: " + srvInterface);
        try {
            Set allServices = new HashSet();
            Filer filer = processingEnv.getFiler();
            String spiFile = SPI_FILES_PATH + srvInterface;
            try {
                FileObject fileObj = filer.getResource(StandardLocation.CLASS_OUTPUT, "", spiFile);
                Set<String> oldServices = readSPIFile(fileObj.openInputStream());
                allServices.addAll(oldServices);
                if (logger.isDebugEnabled()) {
                    StringBuilder sb = new StringBuilder("SPI file content: \n");
                    for (String s : oldServices) {
                        sb.append(" >").append(s).append("\n");
                    }
                    logger.debug(sb.toString());
                }
            } catch (IOException e) {
                logger.debug("SPI file is not exists");
            }

            allServices.addAll(srvClasses);

            FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", spiFile);
            try (OutputStream out = fileObject.openOutputStream()) {
                writeSPIFile(out, allServices);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
