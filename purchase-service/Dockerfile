FROM openjdk:8-alpine
COPY "./target/purchase-service-0.0.1-SNAPSHOT.jar" "apppurchase-service.jar"
EXPOSE 8092
ENTRYPOINT ["java","-jar","apppurchase-service.jar"]