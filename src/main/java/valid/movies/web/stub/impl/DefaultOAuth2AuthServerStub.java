package valid.movies.web.stub.impl;

import okhttp3.*;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import valid.movies.web.stub.OAuth2AuthServerStub;

import java.io.IOException;

@Component
public class DefaultOAuth2AuthServerStub implements OAuth2AuthServerStub {

    private OkHttpClient okHttpClient;

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Value("${security.oauth2.client.grant-type}")
    private String authorizationGrantType;

    @Value("${security.oauth2.client.access-token-uri}")
    private String tokenUri;

    @Autowired
    public DefaultOAuth2AuthServerStub(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public String call(String username, String password) throws IOException, HttpException {

        String credential = Credentials.basic(clientId, clientSecret);

        OkHttpClient client = okHttpClient.newBuilder().build();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("grant_type", authorizationGrantType)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .build();

        Request request = new Request.Builder()
                .url(tokenUri)
                .method("POST", body)
                .addHeader("Authorization", credential)
                .build();

        Response response = client.newCall(request).execute();
        String text = response.body().string();

        if (response.code() != HttpStatus.OK.value()) {
            throw new HttpException(text);
        }

        return text;
    }
}
