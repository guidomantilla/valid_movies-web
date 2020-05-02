FROM openjdk:8-jdk-alpine

# Set necessary environment variables needed for our running image
ENV VALID_APP_NAME='valid-web' \
    VALID_MOVIES_OAUTH2_HOSTNAME='valid-oauth2' \
    VALID_MOVIES_OAUTH2_PORT='8443' \
    VALID_MOVIES_OAUTH2_USERNAME='VALID_MOVIE_RENTAL_WEB' \
    VALID_MOVIES_OAUTH2_PASSWORD='VALID_MOVIE_RENTAL_WEB' \
    VALID_MOVIES_OAUTH2_URL='https://${VALID_MOVIES_OAUTH2_HOSTNAME}:${VALID_MOVIES_OAUTH2_PORT}/oauth' \
    VALID_MOVIES_API_HOSTNAME='valid-movies' \
    VALID_MOVIES_API_PORT='8443' \
    VALID_MOVIES_API_URL='https://${VALID_MOVIES_API_HOSTNAME}:${VALID_MOVIES_API_PORT}' \
    VALID_MOVIES_ENVIRONMENT='dev'

RUN apk --no-cache add curl

VOLUME /tmp

ARG JAR_FILE=build/libs/${VALID_APP_NAME}.jar

ADD ${JAR_FILE} valid-web.jar

EXPOSE 8443

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/valid-web.jar"]
