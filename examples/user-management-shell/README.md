# Absio Broker User Management Shell

This application acts as an example of the Absio SDK in conjunction with the Absio Broker server. The utility shell demonstatres the capabilities of the SDK and server to register new users, handle authentication and credentials, and view and manage public keys.

##Getting Started

The user management shell uses [Spring Shell](https://projects.spring.io/spring-shell/), a simplistic interactive REPL terminal based on [Spring](https://spring.io/). For more information or troublesheeting, see their website.

##Running the application

The User Management shell can be run from a modern IDE (e.g. Eclipse, Intellij IDEA), or using the command line.

```
./mvnw clean install -DskipTests
java -jar target/user-management-shell-0.0.1-SNAPSHOT.jar
```