# Colesico-framework

`Colesico-framework` is a full stack microframework for Java. It is inspired by Spring and EJB frameworks.
The purpose of this framework is to provide simpler and lightweight alternative.

Colesico-framework is designed as a lightweight framework for creating server side / web applications in Java 11+.

## Documentation

 [Colesico-framework docs/manual](https://github.com/colesico/colesico-framework/blob/master/doc/src/asciidoc/framework.asciidoc)

## Examples

 [Colesico-framework examples](https://github.com/colesico/colesico-framework/tree/master/examples)

## Builds

To build colesico framework with maven:

```bash
$ mvn clean install
```

## Maven pom.xml

## Maven dependency

```xml
<dependency>
  <groupId>net.colesico.framework</groupId>
  <artifactId>colesico-appbundle</artifactId>
  <version>1.3.3</version>
</dependency>
```

And for extra framework modules:

```xml
<dependency>
  <groupId>net.colesico.framework</groupId>
  <artifactId>colesico-[extra module]</artifactId>
  <version>1.3.3</version>
</dependency>
```


## License

This project is licensed under the
[Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).

## Versioning

We use [SemVer](http://semver.org/) for versioning.
