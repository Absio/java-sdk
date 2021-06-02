# Key Management Sample Application	

## About
The intent of this Swing application is to show how to use all of the features of the Key Management portion of the SDK.

## Java
This SDK was written with the Java 7 language level, but was tested exclusively against Java 8.  This may not work for Java versions greater than 8.

## Java Cryptography Extension (JCE) Unlimited Strength
In order to use the SDK to perform any cryptography, the JDK/JRE must be updated for the [Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)(this is for JDK 1.8).

## Binary Data
Since there is no easy way to enter or display binary data, most of the inputs/output will either allow for HEX or files (input/output)

## Compiling and Running on Windows

1. Java JDK 8
    1. Download and install [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 8 (we test with OpenJDK)
        1. If using Oracle's JDK 8, install the [JCE Unlimited Strength Jurisdiction Policy Files](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)
    1. Create a JAVA_HOME environment variable and point it to the JDK installation folder (e.g. "C:\Program Files\Java\jdk1.8.0_161")
1. Maven    
    1. Download and unpack [Maven](http://maven.apache.org/download.cgi) to a local folder
    1. Create an M2_HOME and MAVEN_HOME environment variables and point them to your Maven folder 
1. Download or clone the [java-sdk Github](https://github.com/Absio/java-sdk) repository
    ```
    git clone https://github.com/Absio/java-sdk.git
    ```
1. Open a command line to the key-management-app directory of the java-sdk examples
    ```
    cd <installLocation>\java-sdk\examples\key-management-app
    ```
1. Compile the key management application JAR
    ```
    mvn -U -s .m2/settings.xml --batch-mode clean package
    ```
1. Run the key management application
    ```
    //32-Bit JDK
    java -jar target/key-management-app-0.0.1-SNAPSHOT-windows_x86_32
        
    //64-Bit JDK
    java -jar target/key-management-app-0.0.1-SNAPSHOT-windows_x86_64
    ```

## Usage
This is a Swing application with a tabbed interface.  Choose the functionality you wish to try out by selecting the relevant tab and entering the appropriate data.