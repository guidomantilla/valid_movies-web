FROM gradle:6.4.1-jdk8 AS builder

ENV APP_HOME='/app/' \
    VALID_APP_NAME='valid-web'

WORKDIR $APP_HOME
COPY . .
RUN gradle build -x test --continue && \
    mv build/libs/$(ls build/libs) build/libs/${VALID_APP_NAME}.jar


FROM openjdk:8-jre-alpine

# Set necessary environment variables needed for running image
ENV APP_HOME='/app/' \
    VALID_APP_NAME='valid-web' \
    VALID_MOVIES_OAUTH2_HOSTNAME='valid-oauth2' \
    VALID_MOVIES_OAUTH2_PORT='8443' \
    VALID_MOVIES_OAUTH2_USERNAME='VALID_MOVIE_RENTAL_WEB' \
    VALID_MOVIES_OAUTH2_PASSWORD='VALID_MOVIE_RENTAL_WEB' \
    VALID_MOVIES_OAUTH2_URL='https://${VALID_MOVIES_OAUTH2_HOSTNAME}:${VALID_MOVIES_OAUTH2_PORT}/oauth' \
    VALID_MOVIES_API_HOSTNAME='valid-movies' \
    VALID_MOVIES_API_PORT='8443' \
    VALID_MOVIES_API_URL='https://${VALID_MOVIES_API_HOSTNAME}:${VALID_MOVIES_API_PORT}' \
    VALID_MOVIES_ENVIRONMENT='dev'

WORKDIR $APP_HOME
COPY --from=builder ${APP_HOME}/build/libs/ $APP_HOME/
RUN mv ${VALID_APP_NAME}.jar valid-web.jar && apk --no-cache add curl

VOLUME /tmp
EXPOSE 8443

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/valid-web.jar"]
