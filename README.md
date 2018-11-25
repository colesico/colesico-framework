# Colesico-framework

`Colesico-framework` is a micro framework for creating server side applications in `Java 9+`

It consists of highly specialized subprojects.

Colesico-ioc is a simple, lightweight dependency injection framework for Java 9 and above.

Key features:

* Reflection is not used at all (code generation at compile time with AnnotationProcessor).
* Dependency injection is constructor-based only.
* No support for cyclic dependency resolving.
* Can be used as plugin-framework;
* Producers based instance creation definition.
* Dependency injection configuration discovery at container start time (SPI based).
* Multiple dependency injection configurations support (e.g. for testing purpose).
* Out-of-the-box Singleton scope(single instance per container), Thread scope (instance per thread) support.
* Named injections support (@Named annotation).
* Injection point support (e.g. for Logger injection).
* Java 9+


## Ioc container

The Ioc container is used to obtain instances with the dependencies injection.

Ioc container can be created with the builder:

IocBuilder iocBuilder = IocBuilder.get();

Builder is not thread safe. But Ioc container is thread safe and can be used in multithreaded environment.

Builder allows to create an Ioc containers configured for using in:

* production (slow container start, fast dependency injection at runtime)
* developing  (fast container start, slow dependency injection at runtime)
* testing  (slow container start, slow dependency injection at runtime, dependencies satisfaction and cyclic dependencies autocheck)

Default Ioc container creation will create the container configured for the production:

```java
 Ioc ioc = iocBuilder.build();
```
Testing purpose container creation example:


```java
iocBuilder.mode(IocBuilder.BuildMode.TESTING);
Ioc ioc = iocBuilder.build();
```

Obtaining instances with injected dependencies is done using methods:
* instance - returns class instance
* provider - returns class instance provider  (javax.inject.Provider<>), is used to "deferred" instance obtaining/creation.
* polyprovider  - returns chain of class instance providers (colesico.framework.ioc.Polyprovider<>)

To specify what instance to return, use the "keys":

* TypeKey - allows to specify instance class.
* NamedKey - allows to specify instance class and named label (@Named)

In the particular case to obtaining an instance from the Ioc container, you can specify just an instance class. (it will be implicitly converted to TypeKey)

### Producers

The Ioc container "finds out" about classes for dependencies injection with the producers.
Producer defines the instance creation way. Producer it is a plain java class annotated with @Producer annotation.

Creating an instance of a class is defined in two ways:
* Add @Produce annotation on a producer class.
* Define producer's public method. 

@Produce annotation specifies an instance class. In this case, the Ioc container will instantiate the instance simply by calling: new MyClass (param1, paramN);

If you need custom logic to create an instance, you should to define a public producer method that should return the instance.
All public methods of the producer are considered as provider-methods of instances of classes and are used by the Ioc container when creating instances.

Producer example:

```java
@Producer
@Produce(MyImplementation.class)
@Produce(MyClass.class)
public class MyProducer {

    // Produce instance of MyInterface  (MyImplementation implements MyInterface) 
    @Singleton
    public MyInterface getMyInstance(MyImplementation impl){
        return impl;
    }

    // Produce instance for named dependency
    @Named("mynamed")
    public MyInterface getMyNamedInstance(MyImplementation impl){
        return impl;
    }
    
    // Manual instance creation MyBean
    public MyBean getMyBean(MyClass dependency1, MyInterface dependency2){
        return new MyBean( dependency1, dependency2);
    }
}
```

### Scopes

The framework supports out-of-the-box the following scopes of instances:

* @Singleton - so-called local singleton. One instance of class per Ioc container.
* @ThreadScoped - one instance of class per thread


To define the instance scope you must specify an scope annotation(@Singleton и др) either on the instance class or on the producer provider-method.

Example:

```java
@Singleton
public class MyBean1 {}

public class MyBean2 {}

@Producer
@Produce(MyBean1.class)
public class MyProducer{
   
   @Singleton
   public MyBean2 getMyBean2(){
      return new MyBean2();
   }
}
```

In this example the Instances of both classes MyBean1 and MyBean2 are singletones.

### Injectable constuctors

Classes for  dependency injection may not have an explicitly defined constructor. Ioc container will use the default constructor to create instances.
If more than one constructor is declared, the one that will be used for dependency injection must be annotated
with @Inject annotation, otherwise the Ioc container will use the first one in the class.
If the constructor is the only the @Inject annotation is optional.

The constructor parameters can be annotated with the @Named annotation (for named dependencies)

### Ranks

To be able to override the creation of instances the mechanism of ranks of producers is used.  
For example, for testing, using stubs, etc., or for plugins that override any functionality.

Each producer has a certain rank. If several producers "produce" instances of the same class,
then the Ioc container to create the instance will use the producer with a higher rank.

The priority of rank  is set by the Ioc container builder.

By default, the builder uses the following ranks:
    * "minor" for low-priority producers
    * "default" for producers without an explicit specified rank
    * "extension" for extension producers that overrides minor/default producers (plugins, etc)
    * "test" for testing purposes when using stubs, etc.
    
"minor" - rank with the lowest priority, "test" - the highest priority.

The Ioc container builder allows to create any chain of ranks.
The rank itself is an simple arbitrary text string.

### Maven pom.xml

Specify dependence:

        <dependency>
            <groupId>colesico.framework</groupId>
            <artifactId>colesico-ioc</artifactId>
            <version>${colesico.version}</version>
        </dependency>
        
Specify annotation processor:

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
                <configuration>
                    <source>${maven.compiler.source}</source>
                    <target>${maven.compiler.target}</target>
                    <encoding>${project.build.sourceEncoding}</encoding>
                    <annotationProcessorPaths>
                        <dependency>
                            <groupId>colesico.framework</groupId>
                            <artifactId>colesico-ioc</artifactId>
                            <version>${colesico.version}</version>
                        </dependency>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>        
        

### Run an application with plugins discovering at startup.

Ioc container discovers producers with  ServiceLoader.
If the application consists of several files (in lib/* dir.)  and the another files (e.g. plugins, modules etc) will be added later 
without rebuilding the main project, in order the Ioc container can discover the all providers in these separate jar files run the application with a command:

java -cp lib/*;myapp-1.0.jar my.app.Main 

When run by a command like:
 
 java -jar  myapp-1.0.jar 
 
producers in external jar files in the folder lib/* will not be discovered.

