PARENT_NAME=$1
if [ -z "$PARENT_NAME" ]; then
    APP_NAME=valid_web
else
    APP_NAME="$PARENT_NAME"
fi

PARENT_PORT=$2
if [ -z "$PARENT_PORT" ]; then
    PORT=7445
else
    PORT="$PARENT_PORT"
fi

PARENT_OAUTH_HOST=$3
if [ -z "$PARENT_OAUTH_HOST" ]; then
    OAUTH_HOST=valid_mysql
else
    OAUTH_HOST="$PARENT_OAUTH_HOST"
fi

PARENT_API_HOST=$4
if [ -z "$PARENT_API_HOST" ]; then
    API_HOST=valid_mysql
else
    API_HOST="$PARENT_API_HOST"
fi

gradle clean build \
&& mv build/libs/$(ls build/libs) build/libs/"$APP_NAME".jar \
&& docker build . -t "$APP_NAME" \
&& docker container rm --force "$APP_NAME"
   docker container run --detach --restart always \
                        --network valid_network \
                        --link "$OAUTH_HOST":valid_oauth2 \
                        --link "$API_HOST":valid_movies \
                        --publish "$PARENT_PORT":8443 \
                        --name "$APP_NAME" "$APP_NAME"
