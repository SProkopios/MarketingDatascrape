# Use an official OpenJDK runtime as a parent image
FROM openjdk:18-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file to the container
COPY target/*.jar app.jar

# Expose the port on which the app will run
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "app.jar"]