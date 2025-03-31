# Using Amazon Corretto JDK 8 on Alpine Linux
FROM amazoncorretto:8-alpine3.17

# Set working directory in container
WORKDIR /usr/app

# Copy file JAR from directory target of Maven in container
COPY ./target/simple-java-maven-app*.jar app.jar

# Open port application
EXPOSE 8080

# Run application Java
ENTRYPOINT ["java", "-jar", "simple-java-maven-app-1.1.0-SNAPSHOT.jar"]
