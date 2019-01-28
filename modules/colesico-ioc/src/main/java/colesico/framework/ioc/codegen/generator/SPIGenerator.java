package colesico.framework.ioc.codegen.generator;

import colesico.framework.assist.codegen.SPIUtils;
import colesico.framework.ioc.codegen.model.IocletElement;
import colesico.framework.ioc.ioclet.Ioclet;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SPIGenerator {

    private final ProcessingEnvironment processingEnv;

    public SPIGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public void generateSPIFile(Collection<IocletElement> ioclets){
        Set<String> iocletClassNames = new HashSet<>();
        for (IocletElement ie:ioclets){
            iocletClassNames.add(ie.getClassName());
        }
        SPIUtils.addService(Ioclet.class.getCanonicalName(),iocletClassNames,processingEnv);
    }
}
