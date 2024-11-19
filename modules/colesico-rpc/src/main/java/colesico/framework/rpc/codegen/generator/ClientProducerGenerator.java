package colesico.framework.rpc.codegen.generator;

import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.rpc.codegen.model.RpcApiElement;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Singleton;

public class ClientProducerGenerator extends FrameworkAbstractGenerator {

    public ClientProducerGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public void generate(RpcApiElement rpcApiElm) {
        String classSimpleName = rpcApiElm.getClientClassSimpleName();
        String packageName = rpcApiElm.getOriginInterface().getPackageName();

        ProducerGenerator prodGen = new ProducerGenerator(packageName, classSimpleName, this.getClass(), getProcessingEnv());

        prodGen.addProduceAnnotation(ClassName.bestGuess(rpcApiElm.getClientClassName()));

        MethodSpec.Builder pmb = prodGen.addImplementMethod("get" + rpcApiElm.getOriginInterface().getSimpleName(),
                TypeName.get(rpcApiElm.getOriginInterface().getOriginType()),
                ClassName.bestGuess(rpcApiElm.getClientClassName()));
        pmb.addAnnotation(Singleton.class);

        prodGen.generate(rpcApiElm.getOriginInterface().unwrap());
    }
}
