module colesico.framework.rpc {

    
    // Inherited in client projects
    requires transitive colesico.framework.service;
    requires com.squareup.javapoet;
    requires java.compiler;
    requires org.apache.commons.lang3;


    exports colesico.framework.rpc;
}