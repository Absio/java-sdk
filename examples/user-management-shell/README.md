# User Management Shell Application	

## About
The intent of this shell application is to show how to use the key features of the Absio SDK and its use with the Absio Broker application.

## Java
This SDK was written with the Java 7 language level, but was tested exclusively against Java 8.

## Java Cryptography Extension (JCE) Unlimited Strength
In order to use the SDK to perform any cryptography, the JDK/JRE must be updated for the [Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)(this is for JDK 1.8).

## Getting Started

The user management shell uses [Spring Shell](https://projects.spring.io/spring-shell/), a simplistic interactive REPL terminal based on [Spring](https://spring.io/). For more information or troublesheeting, see their website.

## Compiling and Running on Windows

1. Java JDK 8+
    1. Download and install [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 8 or higher
        1. If using JDK 8, install the [JCE Unlimited Strength Jurisdiction Policy Files](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)
    1. Create a JAVA_HOME environment variable and point it to the JDK installation folder (e.g. "C:\Program Files\Java\jdk1.8.0_161")
1. Maven    
    1. Download and unpack [Maven](http://maven.apache.org/download.cgi) to a local folder
    1. Create an M2_HOME and MAVEN_HOME environment variables and point them to your Maven folder 
1. Download or clone the [java-sdk Github](https://github.com/Absio/java-sdk) repository
    ```
    git clone https://github.com/Absio/java-sdk.git
    ```
1. Open a command line to the user-management-shell directory of the java-sdk examples
    ```
    cd <installLocation>\java-sdk\examples\user-management-shell
    ```
1. Compile the user management shell JAR
    ```
    ./mvnw clean install -DskipTests
    ```
1. Run the user management shell
    ```
    java -jar target/user-management-shell-0.0.1-SNAPSHOT.jar
    ```

## Invoking Commands
The shell provides a variety of commands for working with the Absio SDK and Broker server. For a full list of commands, use the "help" command to get more information. All commands are disabled until the session has been initialized (see #Selecting a Provider), and most other commands cannot be accessed until authenticated via "register" or "login".

## Selecting a Provider
Before the shell can be utilized, a data provider must be initialized: the server-only provider (see command "init-server"), OFS-only provider (see command "init-ofs"), or server-provider with a local OFS cache (see command "init-server-cache-ofs").

## Running the Test Script
The source code contains a test script for previewing the major commands against all three providers. The script can be run from the shell with ```script test.script```.