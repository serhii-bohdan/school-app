FROM eclipse-temurin:17-jdk-jammy as base

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src/ src

RUN ./mvnw -B dependency:resolve dependency:resolve-plugins
RUN ./mvnw package -DskipTests

CMD ["java", "-jar", "target/school-app-0.0.1-SNAPSHOT.jar"]
