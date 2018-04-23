# User Management Shell Application	

## About
The intent of this shell application is to show how to use the key features of the Absio SDK and its use with the Absio Broker application.

## Java
This SDK was written with the Java 7 language level, but was tested exclusively against Java 8.

## Java Cryptography Extension (JCE) Unlimited Strength
In order to use the SDK to perform any cryptography, the JDK/JRE must be updated for the [Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)(this is for JDK 1.8).

## Getting Started

The user management shell uses [Spring Shell](https://projects.spring.io/spring-shell/), a simplistic interactive REPL terminal based on [Spring](https://spring.io/). For more information or troublesheeting, see their website.

## Running the application

The User Management shell can be run from a modern IDE (e.g. Eclipse, Intellij IDEA), or using the command line.

```
./mvnw clean install -DskipTests
java -jar target/user-management-shell-0.0.1-SNAPSHOT.jar
```

## Invoking Commands
The shell provides a variety of commands for working with the Absio SDK and Broker server. For a full list of commands, use the "help" command to get more information. All commands are disabled until "initialize" is called, and most other commands cannot be accessed until authenticated via "register" or "login".
