FROM eclipse-temurin:17-alpine
COPY target/app.jar /app.jar
# This is the port that your javalin application will listen on
EXPOSE 7000
ENTRYPOINT ["java", "-jar", "/app.jar"]