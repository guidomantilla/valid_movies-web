FROM openjdk:8-jdk-alpine

# Set necessary environment variables needed for our running image
ENV VALID_MOVIES_OAUTH2_USERNAME='VALID_MOVIE_RENTAL_WEB' \
    VALID_MOVIES_OAUTH2_PASSWORD='VALID_MOVIE_RENTAL_WEB' \
    VALID_MOVIES_OAUTH2_URL='https://valid_oauth2:8443/oauth' \
    VALID_MOVIES_API_URL='https://valid_movies:8443' \
    VALID_MOVIES_ENVIRONMENT='dev'

VOLUME /tmp

ARG JAR_FILE=build/libs/valid_web.jar

ADD ${JAR_FILE} app.jar

EXPOSE 8443

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
