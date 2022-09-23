FROM openjdk:8-alpine
COPY "./target/eureka-server-0.0.1-SNAPSHOT.jar" "appeureka-server.jar"
EXPOSE 8761
ENTRYPOINT ["java","-jar","appeureka-server.jar"]