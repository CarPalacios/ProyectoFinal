FROM openjdk:8-alpine
COPY "./target/withdrawal-service-0.0.1-SNAPSHOT.jar" "appwithdrawal-service.jar"
EXPOSE 8095
ENTRYPOINT ["java","-jar","appwithdrawal-service.jar"]