# docker image for the backend

# build the jar
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw -q clean package -DskipTests

# run the jar
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/careernest-0.0.1-SNAPSHOT.jar app.jar
# the env variables are set in render
ENTRYPOINT ["java", "-jar", "app.jar"]
