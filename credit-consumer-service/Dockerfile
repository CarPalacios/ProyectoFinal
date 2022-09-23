FROM openjdk:8-alpine
COPY "./target/credit-consumer-service-0.0.1-SNAPSHOT.jar" "appcredit-consumer-service.jar"
EXPOSE 8097
ENTRYPOINT ["java","-jar","appcredit-consumer-service.jar"]