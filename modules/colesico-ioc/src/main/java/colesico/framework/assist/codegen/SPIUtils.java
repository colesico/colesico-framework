package colesico.framework.assist.codegen;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.*;

public class SPIUtils {

    public static final String SPI_FILES_PATH = "META-INF/services/";

    static final Logger logger = LoggerFactory.getLogger(SPIUtils.class);

    public static Set<String> readSPIFile(InputStream input) {
        Set<String> srvClasses = new HashSet();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(input, "UTF-8"))) {
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
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
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

    public static void addService(String srvInterface, Set<String> srvClasses, ProcessingEnvironment processingEnv) {
        logger.debug("Generate SPI file for: "+srvInterface);
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
