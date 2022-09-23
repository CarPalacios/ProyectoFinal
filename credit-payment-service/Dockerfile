FROM openjdk:8-alpine
COPY "./target/credit-payment-service-0.0.1-SNAPSHOT.jar" "appcredit-payment-service.jar"
EXPOSE 8098
ENTRYPOINT ["java","-jar","appcredit-payment-service.jar"]