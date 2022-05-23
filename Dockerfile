FROM openjdk:11
ARG JAR_FILE=target/*.jar
RUN mkdir /app
RUN chown -R 1001 /app
RUN chmod 777 /app

# set working directory
WORKDIR /app
COPY ${JAR_FILE} /app/app.jar

USER 1001
ENTRYPOINT ["java","-jar","/app/app.jar"]