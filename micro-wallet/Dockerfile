FROM openjdk:8-alpine
COPY "./target/micro-wallet-0.0.1-SNAPSHOT.jar" "appmicro-wallet.jar"
EXPOSE 8099
ENTRYPOINT ["java","-jar","appmicro-wallet.jar"]