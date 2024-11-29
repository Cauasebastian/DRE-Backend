# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Define PostgreSQL-related environment variables
ENV SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/dredatabase
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=password
ENV SPRING_PROFILES_ACTIVE=dev
ENV CORS_ALLOWED_ORIGINS=http://localhost:3000

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]