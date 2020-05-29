# Colesico-framework

`Colesico-framework` is a lightweight full stack microframework for creating backend and server side web applications in Java 14+.
It is inspired by Spring and EJB frameworks but uses more simplified approach to construct an applications. The purpose of this framework is to provide simpler and lightweight alternative. 

## Documentation

 [Colesico-framework docs/manual](https://github.com/colesico/colesico-framework/blob/master/docs/src/asciidoc/framework.adoc)

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
  <version>${colesico-version}</version>
</dependency>
```

[${colesico-version}](https://search.maven.org/artifact/net.colesico.framework/colesico-framework)

## License

This project is licensed under the
[Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).

## Versioning policy

This project uses modified semver approach.

Version number: 

[major].[moderate].[minor]

* [major] - Massive changes affecting many framework modules or a complete change of architecture.
            Requires significant client code changes.
* [moderate] - Minimal loss of backward compatibility that affects a few framework modules. 
            Changes may require a few lines updating in the client code to make it works.
            This also may not break an application completely, but it may cause partial degradation. 
            Please, pay attention to check your code is working properly on such updates.
* [minor] - Backward compatible changes that do not require a client code updates to make it work.
            This may be a bugfixes, new compatible framework features, documentation updates, etc.


Update 1.2.3 to 1.2.4 - should not require any client code changes.

Update 1.2.3 to 1.3.0 - may require some code changes.

Update 1.2.3 to 2.0.0 - require significant client code changes.


