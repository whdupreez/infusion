
Apache Karaf Features
=====================

Version 0.2

Copyright &copy; 2013 Willy du Preez

2013-11-16

## Revision History

Version     | Date        | Author         | Description
:---------: | ----------- | -------------- | ---------------------------
0.1         | 2013-11-13  | Willy du Preez | Created
0..2        | 2013-11-16  | Willy du Preez | Enchancements and bug fixes

> **Creating Apache Karaf features**

>This tutorial gives an overview of creating, installing and configuring Apache Karaf features using maven.

## Table of Contents

[TOC]

## Features Overview

### What is a Feature

Apache Karaf allows you to group bundles into an easily installable application or "feature". Features are managed using the Karaf "features" shell extension. Allows easy provisioning.

### Example Project

An example project is available on github:
>https://github.com/whdupreez/examples-karaf-feature

## Creating a Feature

### Features Namespace

The features schema is available in the Karaf sources and publicly available at:
>http://karaf.apache.org/xmlns/features/v1.0.0

### Defining a Feature Repository

A feature repository defines installable features. It can also define optional dependencies on other features.

```
<features
	xmlns="http://karaf.apache.org/xmlns/features/v1.0.0"
	xmlxsi:schemaLocation="http://karaf.apache.org/xmlns/features/v1.0.0 http://karaf.apache.org/xmlns/features/v1.0.0"
	name="Optional feature repository name">

    <!-- features and references to other feature repositories go here -->

</features>
```

### Defining a Feature

A feature is defined in a feature repository using the *feature* element.

```
<features>
    <feature name="my-feature" version="0.0.1">

        <!-- bundles, feature dependencies, config, etc. go here -->

	</feature>
</features>
```

Bundles that form part of a feature are defined in the *feature* element using the *bundle* element. The *bundle* element contains the URL where the bundle JAR is located.

```
<features>
    <feature name="my-feature" version="0.0.1">
        <bundle>mvn:org.apache.commons/commons-lang3/3.1</bundle>
        <bundle>mvn:com.willydupreez.karaf/example/0.0.1-SNAPSHOT</bundle>
	</feature>
</features>
```
Any of the Karaf URL handlers could be used to specify the bundle location. Some of the common protocols are mvn (provided by the Maven URL handler), file, http, and wrap. Additional URL handlers are provided by OPS4J Pax URL.

### Dependencies on other Features

A feature can also depend on other features. The additional features will be installed as part of the application.

#### Internal Feature Dependencies

Features in the same feature repository can be referenced directly using the nested *feature* element.

```
<features>
    <feature name="my-second-feature" version="0.0.1">
        <feature version="0.0.1">my-feature</feature>
        <!-- additional bundles, feature dependencies, config, etc. -->
	</feature>
</features>
```

If no version is specified, the latest available version of the feature is installed. Note that a version range can also be specified, i.e. "[1.0.0,2)"

#### External Feature Dependencies

A feature can also reference features from other feature repositories. To reference these features, the repository location has to be defined using the *repository* element. The *repository* element is a child element of the root *features* element.

```
<features>

    <repository>mvn:org.apache.cxf.karaf/apache-cxf/2.7.7/xml/features</repository>

    <feature name="my-second-feature" version="0.0.1">

        <!-- external feature dependencies -->
        <feature version="2.7.7">cxf</feature>
    	<feature version="2.7.7">cxf-http-async</feature>

        <!-- additional bundles, feature dependencies, config, etc. -->

	</feature>

</features>
```

### Feature Configuration

A feature can be configured using the *feature* child elements *config* and *configfile*.

## Building Features using Maven

### Processing and Filtering

A *features.xml* can be processed using the **maven-resources-plugin**.

```
<build>
    <plugins>
		<plugin>
			<artifactId>maven-resources-plugin</artifactId>
			<executions>
				<execution>
					<id>copy-resources</id>
					<phase>validate</phase>
					<goals>
						<goal>copy-resources</goal>
					</goals>
					<configuration>
						<outputDirectory>${basedir}/target</outputDirectory>
						<resources>
							<resource>
								<directory>src/main/assembly</directory>
								<filtering>true</filtering>
							</resource>
						</resources>
					</configuration>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
```

This allows the use of maven properties in the *features.xml* file. The benefit of this approach is simplified version management, i.e.:

```
<features>

	<repository>mvn:org.apache.cxf.karaf/apache-cxf/${cxf.version}/xml/features</repository>

    <feature name="my-feature" version="${version}">

		<!-- External Features -->
    	<feature version="${cxf.version}">cxf</feature>
    	<feature version="${cxf.version}">cxf-http-async</feature>

		<!-- External Bundle Dependencies -->
		<bundle>mvn:org.apache.commons/commons-lang3/${commons-lang3.version}</bundle>

        <bundle>mvn:com.willydupreez.karaf/example/${version}</bundle>

    </feature>

</features>
```

### Attaching the *features.xml* Artifact

The *features.xml* (repository definition) can be attached to a maven artifact using the **build-helper-maven-plugin**.

```
<build>
    <plugins>
		<plugin>
			<groupId>org.codehaus.mojo</groupId>
			<artifactId>build-helper-maven-plugin</artifactId>
			<executions>
				<execution>
					<id>attach-artifacts</id>
					<phase>package</phase>
					<goals>
						<goal>attach-artifact</goal>
					</goals>
					<configuration>
						<artifacts>
							<artifact>
								<file>target/features.xml</file>
								<type>xml</type>
								<classifier>features</classifier>
							</artifact>
						</artifacts>
					</configuration>
				</execution>
			</executions>
		</plugin>
    </plugins>
</build>
```

## Installing a Feature
