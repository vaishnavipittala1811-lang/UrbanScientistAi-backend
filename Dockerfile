# ==========================================
# Multi-stage Dockerfile for Render Free Tier
# Optimized for 512MB RAM free environment
# ==========================================

# Stage 1: Build the Spring Boot application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Cache dependencies layer
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build application jar
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Minimal Production JRE Runtime (~150MB)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-privileged application user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /app/target/*.jar app.jar
RUN chown -R appuser:appgroup /app

USER appuser

# Tuning for 512MB RAM free tier:
# - SerialGC uses minimal memory overhead (<30MB GC metadata)
# - Xss512k cuts per-thread stack memory in half
# - MaxRAMPercentage=70.0 ensures JVM leaves 150MB+ for OS/native buffers
ENV JAVA_OPTS="-XX:+UseSerialGC -Xss512k -XX:MaxRAMPercentage=70.0 -XX:+ExitOnOutOfMemoryError"
ENV PORT=8081

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
