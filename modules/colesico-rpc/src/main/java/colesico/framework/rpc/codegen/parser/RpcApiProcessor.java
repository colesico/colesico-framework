package colesico.framework.rpc.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.ioc.codegen.model.IocletElement;
import colesico.framework.ioc.production.Producer;
import colesico.framework.rpc.RpcApi;
import colesico.framework.rpc.codegen.generator.RpcSchemeGenerator;
import colesico.framework.rpc.codegen.model.RpcApiElement;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Set;

public class RpcApiProcessor extends FrameworkAbstractProcessor {

    private RpcApiParser rpcApiParser;
    private RpcSchemeGenerator rpcSchemeGenerator;

    @Override
    protected void onInit() {
        rpcApiParser = new RpcApiParser(processingEnv);
        rpcSchemeGenerator = new RpcSchemeGenerator(processingEnv);
    }

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class[]{RpcApi.class};
    }

    @Override
    public boolean process(Set<? extends TypeElement> elms, RoundEnvironment roundEnv) {
        logger.debug("Start RPC API interfaces processing...");
        boolean result = false;

        for (Element elm : roundEnv.getElementsAnnotatedWith(RpcApi.class)) {
            if (elm.getKind() != ElementKind.INTERFACE) {
                continue;
            }
            TypeElement producerElement;
            try {
                producerElement = (TypeElement) elm;
                RpcApiElement parsedElement = rpcApiParser.parse(ClassElement.fromElement(processingEnv, producerElement));
                rpcSchemeGenerator.generate(parsedElement);
            } catch (CodegenException ce) {
                String message = "Error processing RPC API interface '" + elm.toString() + "': " + ce.getMessage();
                logger.debug(message);
                ce.print(processingEnv, elm);
                return false;
            } catch (Exception e) {
                String msg = ExceptionUtils.getRootCauseMessage(e);
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
                if (logger.isDebugEnabled()) {
                    e.printStackTrace();
                }
                return false;
            }
        }

        return true;
    }
}
