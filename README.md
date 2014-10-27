java-lattices [![Build Status](https://travis-ci.org/kbertet/java-lattices.png?branch=master)](https://travis-ci.org/kbertet/java-lattices) ![Version](http://img.shields.io/badge/version-2.0.0--SNAPSHOT-blue.svg) [![License](http://img.shields.io/badge/license-CeCILL--B-red.svg)](http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html)
==============

See [documentation](http://kbertet.github.io/java-lattices).

Add
~~~xml

<dependencies>
	<dependency>
		<groupId>fr.kbertet</groupId>
		<artifactId>lattices</artifactId>
		<version>2.0.0-SNAPSHOT</version>
	</dependency>
</dependencies>

<repositories>
	<repository>
		<id>java-lattices-mvn-repo</id>
		<name>lattices repository</name>
		<layout>default</layout>
		<url>https://raw.githubusercontent.com/kbertet/java-lattices/mvn-repo/</url>
		<snapshots>
			<enabled>true</enabled>
			<updatePolicy>always</updatePolicy>
			<checksumPolicy>fail</checksumPolicy>
		</snapshots>
		<releases>
			<enabled>true</enabled>
			<updatePolicy>never</updatePolicy>
			<checksumPolicy>fail</checksumPolicy>
		</releases>
	</repository>
</repositories>
~~~

to your `pom.xml` file for use with maven.
