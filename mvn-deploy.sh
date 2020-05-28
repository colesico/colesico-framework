#!/bin/bash

# see https://central.sonatype.org/pages/ossrh-guide.html
# and https://central.sonatype.org/pages/apache-maven.html
#
# To deploy with javadoc, sources and asc pgp signature create mvn master password:
# mvn --encrypt-master-password <password> and add it to .m2/settings-security.xml, example:
# 
# <settingsSecurity>
#    <master>{encripted+master+pwd=}</master>
# </settingsSecurity>
# 
# Then encript colesico jira's password with mvn --encrypt-password <password> and add it to .m2/settings.xml:
#
# <settings>
#   <servers>
#     <server>
#       <id>ossrh</id>
#       <username>colesico</username>
#       <password>{jira+encripted+pwd=}</password>
#     </server>
#   </servers>
# 
#   <profiles>
#      <profile>
#        <id>gpg-conf</id>
#        <activation><activeByDefault>true</activeByDefault></activation>
#        <properties>
#          <gpg.executable>gpg</gpg.executable>
#          <gpg.passphrase>private-key-passphrase</gpg.passphrase>
#        </properties>
#      </profile>
#   </profiles>
# </settings>
#
# and run:

mvn clean deploy -P modules,bundles,release