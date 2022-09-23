FROM openjdk:8-alpine
COPY "./target/product-service-0.0.1-SNAPSHOT.jar" "appproduct-service.jar"
EXPOSE 8091
ENTRYPOINT ["java","-jar","appproduct-service.jar"]