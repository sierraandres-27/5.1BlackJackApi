FROM eclipse-temurin:21.0.6_7-jdk

EXPOSE 8080

WORKDIR /root

COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root

RUN ./mvnw dependency:go-offline

COPY ./src /root/src

RUN ./mvnw clean install -DskipTests

COPY ./target/S05T01Blackjack-0.0.1-SNAPSHOT.jar /root/S05T01Blackjack-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/root/S05T01Blackjack-0.0.1-SNAPSHOT.jar"]
