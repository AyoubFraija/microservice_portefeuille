FROM openjdk:17-jdk

COPY target/Gestion_Portefeuilles-0.0.1-SNAPSHOT.jar .

EXPOSE 8043

ENTRYPOINT ["java", "-jar", "Gestion_Portefeuilles-0.0.1-SNAPSHOT.jar"]