# ---- Stage 1: Build ----
FROM maven:3.9.9-amazoncorretto-21 AS builder

# Set working directory inside the build container
WORKDIR /build

# Copy Maven project and build it
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Stage 2: Runtime ----
FROM amazoncorretto:21.0.9-alpine

# Set a non-root user for better security
RUN adduser -D spring
USER spring

# Set working directory for the app
WORKDIR /app

# Copy only the built JAR from the builder image
COPY --from=builder /build/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Start the Spring Boot application
# ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]