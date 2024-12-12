# Use an official OpenJDK runtime as a parent image
FROM openjdk:17

# Set the working directory to /app
WORKDIR /walletService

# Copy the current directory contents into the container at /walletService
COPY . .

# Install Maven
RUN microdnf install -y maven

RUN mvn clean install

ENTRYPOINT ["java" ,"-jar","target/walletService-0.0.1-SNAPSHOT.jar"]

