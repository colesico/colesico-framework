# Colesico-framework

`Colesico-framework` ia a lightweight full stack microframework for creating server side backend and web applications in Java 10+.
It is inspired by Spring and EJB frameworks but uses more simplified approach to construct an applications. The purpose of this framework is to provide simpler and lightweight alternative. 

## Documentation

 [Colesico-framework docs/manual](https://github.com/colesico/colesico-framework/blob/master/doc/src/asciidoc/framework.asciidoc)

## Examples

 [Colesico-framework examples](https://github.com/colesico/colesico-framework/tree/master/examples)

## Builds

To build colesico framework with maven:

```bash
$ mvn clean install
```

## Maven dependency

```xml
<dependency>
  <groupId>net.colesico.framework</groupId>
  <artifactId>colesico-appbundle</artifactId>
  <version>1.7.2</version>
</dependency>
```

For extra framework modules:

```xml
<dependency>
  <groupId>net.colesico.framework</groupId>
  <artifactId>colesico-[extra module]</artifactId>
  <version>1.7.2</version>
</dependency>
```


## License

This project is licensed under the
[Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).

## Versioning

We use [SemVer](http://semver.org/) for versioning.
