# Absio Key Management Application

This application acts as an example of the Absio SDK for use with various cryptographic methods.

##Getting Started

The user management shell uses [Spring Shell](https://projects.spring.io/spring-shell/), a simplistic interactive REPL terminal based on [Spring](https://spring.io/). For more information or troublesheeting, see their website.

##Running the application

The Key Management application can be run from a modern IDE (e.g. Eclipse, Intellij IDEA), or using the command line.

### Command Line

1. Build the application from the source code
    ```
    mvn clean install
    ```
2. Gather all dependent JARs
    * com.absio - absio-sdk-win32-1.2.0-SNAPSHOT.jar or absio-sdk-win64 dependent on Java framework
    * com.intellij - forms_rt-7.0.3.jar
    * target\key-management-app-0.0.1-SNAPSHOT.jar
3. Run the application
    ```
    //32 Bit
    %JAVA32_HOME%/bin/java.exe -cp absio-sdk-win32-1.2.0-SNAPSHOT.jar;key-management-app-0.0.1-SNAPSHOT.jar;forms_rt-7.0.3.jar com.absio.crypto.KeyManagementSampleApplication
    
    //64 Bit
    %JAVA64_HOME%/bin/java.exe -cp absio-sdk-win64-1.2.0-SNAPSHOT.jar;key-management-app-0.0.1-SNAPSHOT.jar;forms_rt-7.0.3.jar com.absio.crypto.KeyManagementSampleApplication
    ```