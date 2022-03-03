#
# Build stage
#
FROM maven AS build
COPY src /home/app/src
COPY pom.xml /home/app
WORKDIR /home/app
RUN mvn install

#
# Package stage
#
FROM openjdk:17.0.2
COPY --from=build /home/app/target/Tic-Tac-Toe-Server-1.0-jar-with-dependencies.jar /home/app/tick-tack-toe.jar
WORKDIR /home/app
EXPOSE 5000 5001
ENTRYPOINT ["java","-jar","tick-tack-toe.jar"]
