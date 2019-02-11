The colesico framework common modules bundle.
This can be used in pom.xml:

<dependencies>
		...
        <dependency>
            <groupId>net.colesico.framework</groupId>
            <artifactId>colesico-appbundle</artifactId>
            <version>${colesico.version}</version>
        </dependency>
		...
<dependencies>		

...
<plugins>
	...
	<plugin>
		<groupId>org.apache.maven.plugins</groupId>
		<artifactId>maven-compiler-plugin</artifactId>
		...
		<configuration>
			...
			<annotationProcessorPaths>
				<path>
					<groupId>net.colesico.framework</groupId>
					<artifactId>colesico-appbundle</artifactId>
					<version>${colesico.version}</version>
				</path>
			</annotationProcessorPaths>	
			...
		<configuration>
		...
	<plugin>
	...
<plugins>	


and in module-info.java:

module my.module{
    ...
	requires transitive colesico.framework.appbundle;
	...
}