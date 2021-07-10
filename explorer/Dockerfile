FROM openjdk:8-jdk-alpine
COPY root /root
COPY target/explorer-1.0-SNAPSHOT.jar /explorer.jar
COPY client /client
CMD ["java", "-jar", "/explorer.jar"]