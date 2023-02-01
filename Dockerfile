#build project
FROM eclipse-temurin:11-jdk as build

RUN mkdir /project

WORKDIR /project

#COPY ./.mvn ./.mvn
#
#COPY mvnw* .
#
#RUN ./mvnw --version

COPY . .

RUN ./mvnw clean package

#run the application
FROM eclipse-temurin:11-jre

RUN mkdir opt/app

COPY --from=build /project/target/*.jar /opt/app/app.jar

WORKDIR /opt/app

ENTRYPOINT ["java", "-jar", "app.jar"]