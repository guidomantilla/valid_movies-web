PARENT_NAME=$1
if [ -z "$PARENT_NAME" ]; then
    APP_NAME=valid_web
else
    APP_NAME="$PARENT_NAME"
fi

PARENT_PORT=$2
if [ -z "$PARENT_PORT" ]; then
    PORT=9443
else
    PORT="$PARENT_PORT"
fi

gradle clean build \
&& mv build/libs/$(ls build/libs) build/libs/"$APP_NAME".jar \
&& docker build . -t "$APP_NAME" \
&& docker container rm --force "$APP_NAME"
   docker container run --detach --restart always \
                        --network valid_network \
                        --publish "$PARENT_PORT":8443 \
                        --name "$APP_NAME" "$APP_NAME"
