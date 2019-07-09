# Project Moonstone

Project **Moonstone** is the open source version of the popular **Arrow Connect Platform** developed by Arrow Electronics.  To learn more about Arrow Connect, please visit the office web site of [Arrow Connect](https://www.arrowconnect.io) and the [Developer Hub](https://developer.arrowconnect.io)

## Build from source code

Project moonstone uses [Gradle](https://gradle.org) build tool.  Build requires **JDK 8**, [NodeJS](https://nodejs.org), and [npm](https://www.npmjs.com)

```
git clone https://github.com/arrow-acs/moonstone.git
cd moonstone
gradle build
```
Binaries (shared libraries and applications) are generated under **build/libs** directory of each sub-projects.  Software is developed using [Spring Framework](https://spring.io).  All applications are [Spring Boot](https://spring.io/projects/spring-boot).
