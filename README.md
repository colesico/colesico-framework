# Colesico-framework

`Colesico-framework` is a lightweight full stack microframework for creating backend and server side web applications in Java 11+.
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
  <artifactId>colesico-[module or bundle]</artifactId>
  <version>2.3.1</version>
</dependency>
```

## License

This project is licensed under the
[Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).

## Versioning

Version number: [major].[minor].[micro]

* [major] - Massive changes affecting many framework 
modules or a complete change of architecture.
Requires significant client code changes.
* [minor] - Minor or no breakages. 
Changes require a few lines updating in the client code to make it work.
This also may not break an application compeletely. But it may cause partial degradation. 
Please, pay attention to check your code is working properly on such updates.
* [micro]  - Changes do not require a client code updates to make it work.
This is a bug fixes or new none beaking framework features.


Update 1.2.3 to 1.2.4 - should not require any client code changes.
Update 1.2.3 to 1.3.0 - should require some code changes.


