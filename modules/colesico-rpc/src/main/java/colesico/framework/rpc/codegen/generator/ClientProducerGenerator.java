package colesico.framework.rpc.codegen.generator;

import colesico.framework.assist.codegen.FrameworkAbstractGenerator;
import colesico.framework.ioc.codegen.generator.ProducerGenerator;
import colesico.framework.rpc.codegen.model.RpcApiElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

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
