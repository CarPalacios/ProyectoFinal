FROM openjdk:8-alpine
COPY "./target/client-service-0.0.1-SNAPSHOT.jar" "appclient-service.jar"
EXPOSE 8090
ENTRYPOINT ["java","-jar","appclient-service.jar"]