FROM openjdk:8-alpine
COPY "./target/account-service-0.0.1-SNAPSHOT.jar" "appaccount-service.jar"
EXPOSE 8094
ENTRYPOINT ["java","-jar","appaccount-service.jar"]