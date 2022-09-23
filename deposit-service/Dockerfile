FROM openjdk:8-alpine
COPY "./target/deposit-service-0.0.1-SNAPSHOT.jar" "appdeposit-service.jar"
EXPOSE 8096
ENTRYPOINT ["java","-jar","appdeposit-service.jar"]