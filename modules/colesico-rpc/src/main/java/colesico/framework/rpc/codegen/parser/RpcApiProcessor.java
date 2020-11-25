package colesico.framework.rpc.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.rpc.RpcApi;
import colesico.framework.rpc.codegen.generator.ClientGenerator;
import colesico.framework.rpc.codegen.generator.EnvelopeGenerator;
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
    private EnvelopeGenerator envelopeGenerator;
    private ClientGenerator clientGenerator;

    @Override
    protected void onInit() {
        rpcApiParser = new RpcApiParser(processingEnv);
        envelopeGenerator = new EnvelopeGenerator(processingEnv);
        clientGenerator = new ClientGenerator(processingEnv);
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
                envelopeGenerator.generate(parsedElement);
                clientGenerator.generate(parsedElement);
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
