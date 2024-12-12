# Use an official OpenJDK runtime as a parent image
FROM openjdk:17

# Set the working directory to /app
WORKDIR /bookingService

# Copy the current directory contents into the container at /userService
COPY . .

# Install Maven
RUN microdnf install -y maven

# Build the projects with Maven
RUN mvn clean install -DskipTests


ENTRYPOINT ["java" ,"-jar","target/bookingService-0.0.1-SNAPSHOT.jar"]

