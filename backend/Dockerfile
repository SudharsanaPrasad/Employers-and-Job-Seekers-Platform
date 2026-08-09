# Backend Docker image for Render. On Render set the service Root Directory to
# "backend" so this Dockerfile and the Maven project are the build context.

# --- build stage: compile and package the jar ---
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw -q clean package -DskipTests

# --- run stage: small JRE image that runs the jar ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/careernest-0.0.1-SNAPSHOT.jar app.jar
# reads MONGODB_URI, JWT_SECRET, TWILIO_* and PORT from the environment
ENTRYPOINT ["java", "-jar", "app.jar"]
