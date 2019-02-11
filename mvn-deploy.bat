rem see https://central.sonatype.org/pages/ossrh-guide.html
rem and https://central.sonatype.org/pages/apache-maven.html
rem
rem To deploy with javadoc, sources and asc pgp signature create mvn master password:
rem mvn --encrypt-master-password <password> and add it to .m2/settings-security.xml, example:
rem 
rem <settingsSecurity>
rem    <master>{encripted+master+pwd=}</master>
rem </settingsSecurity>
rem 
rem Then encript colesico jira's password with mvn --encrypt-password <password> and add it to .m2/settings.xml:
rem
rem <settings>
rem   <servers>
rem     <server>
rem       <id>ossrh</id>
rem       <username>colesico</username>
rem       <password>{jira+encripted+pwd=}</password>
rem     </server>
rem   </servers>
rem 
rem   <profiles>
rem      <profile>
rem        <id>gpg-conf</id>
rem        <activation><activeByDefault>true</activeByDefault></activation>
rem        <properties>
rem          <gpg.executable>gpg</gpg.executable>
rem          <gpg.passphrase>private-key-passphrase</gpg.passphrase>
rem        </properties>
rem      </profile>
rem   </profiles>
rem </settings>
rem
rem and run:

mvn clean deploy -P modules,bundles,release