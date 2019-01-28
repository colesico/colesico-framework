package colesico.examples.transaction;

import colesico.framework.ioc.Producer;
import colesico.framework.transaction.TransactionalShell;

import javax.inject.Named;
import javax.inject.Singleton;

@Producer
public class MyProducer {

    /**
     * Define default transactional shell
     * @return
     */
    @Singleton
    public TransactionalShell getTxExec1(){
        return new TransctionalShellMock("Default");
    }

    @Singleton
    @Named("custom")
    public TransactionalShell getTxExec2(){
        return new TransctionalShellMock("Custom");
    }

    @Singleton
    @Named("alternative")
    public TransactionalShell getTxExec3(){
        return new TransctionalShellMock("MyTx...");
    }
}
