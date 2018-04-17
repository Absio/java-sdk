# Key Management Sample Application	

## About
The intent of this Swing application is to show how to use all of the features of the Key Management portion of the SDK.

## Java
This SDK was written with the Java 7 language level, but was tested exclusively against Java 8.

## Java Cryptography Extension (JCE) Unlimited Strength
In order to use the SDK to perform any cryptography, the JDK/JRE must be updated for the [Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)(this is for JDK 1.8).

## Binary Data
Since there is no easy way to enter or display binary data, most of the inputs/output will either allow for HEX or files (input/output)

## Running
Make sure you have a 32 or 64 bit version of Java 8 installed.  Download the appropriate version of the SDK from the [Absio Nexus](https://nexus.absio.com/):<br>
Build the application from the source code
```
mvn clean install
```
Gather all dependency JARs:
* [32 bit combined Java SDK](https://nexus.absio.com/#browse/browse:maven-releases:com%2Fabsio%2Fabsio-sdk-win32) or [64 bit combined Java SDK](https://nexus.absio.com/#browse/browse:maven-releases:com%2Fabsio%2Fabsio-sdk-win64)<br>
* [UiDesigner Jar](http://central.maven.org/maven2/com/intellij/forms_rt/7.0.3/forms_rt-7.0.3.jar)<br><br>
* key-management-app-0.0.1-SNAPSHOT.jar
Take all artifacts and copy into the same directory. Then run with the following command:
```
//32 Bit
%JAVA32_HOME%/bin/java.exe -cp absio-sdk-win32-1.2.0.jar;key-management-app-0.0.1-SNAPSHOT.jar;forms_rt-7.0.3.jar com.absio.crypto.KeyManagementSampleApplication
    
//64 Bit
%JAVA64_HOME%/bin/java.exe -cp absio-sdk-win64-1.2.0.jar;key-management-app-0.0.1-SNAPSHOT.jar;forms_rt-7.0.3.jar com.absio.crypto.KeyManagementSampleApplication
```

## Usage
This is a Swing application with a tabbed interface.  Choose the functionality you wish to try out by selecting the relevant tab and entering the appropriate data.