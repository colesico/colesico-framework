rem To install with javadoc, sources and asc gpg signature add to .m2/settings.xml gpg plugin configuration
rem 
rem <profiles>
rem    <profile>
rem      <id>gpg-conf</id>
rem      <activation><activeByDefault>true</activeByDefault></activation>
rem      <properties>
rem        <gpg.executable>gpg</gpg.executable>
rem        <gpg.passphrase>my-private-key-passphrase</gpg.passphrase>
rem      </properties>
rem    </profile>
rem </profiles>
rem and run:
rem mvn clean install -P modules,bundles,examples,doc,release

mvn clean install -P modules,bundles,examples,doc