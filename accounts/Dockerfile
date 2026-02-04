FROM amazoncorretto:25-alpine-jdk

LABEL maintainer="mdMoldova"   

COPY target/accounts-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]