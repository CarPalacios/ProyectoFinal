FROM openjdk:8-alpine
COPY "./target/config-server-0.0.1-SNAPSHOT.jar" "app-config-server.jar"
EXPOSE 8081
ENTRYPOINT ["java","-jar","app-config-server.jar"]