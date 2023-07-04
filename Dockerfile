FROM maven:3.8.3-openjdk-17

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn -B dependency:resolve dependency:resolve-plugins
RUN mvn package -DskipTests

CMD ["java", "-jar", "target/school-app-0.0.1-SNAPSHOT-jar-with-dependencies.jar"]