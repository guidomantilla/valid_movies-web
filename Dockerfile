FROM openjdk:8-jdk-alpine

# Set necessary environment variables needed for our running image
ENV VALID_MOVIES_API_URL='jdbc:mysql://valid_mysql:3306/valid-movie-rental?useSSL=false&allowPublicKeyRetrieval=true' \
    VALID_MOVIES_OAUTH2_URL='root' \
    VALID_MOVIES_DATASOURCE_PASSWORD='v4l1d-gu1d0-m4nt*' \
    VALID_MOVIES_ENVIRONMENT='dev'

VOLUME /tmp

ARG JAR_FILE=build/libs/valid_movies.jar

ADD ${JAR_FILE} app.jar

EXPOSE 8443

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
