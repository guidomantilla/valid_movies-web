package valid.movies.web.stub.impl;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import valid.movies.web.stub.MoviesApiStub;

import java.io.IOException;

@Component
public class DefaultMoviesApiStub implements MoviesApiStub {

    private OkHttpClient okHttpClient;

    @Value("${endpoint.movies.get-films}")
    private String moviesApiGetFilms;

    @Autowired
    public DefaultMoviesApiStub(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public String getFilms(String bearerToken) throws IOException, HttpException {

        OkHttpClient client = okHttpClient.newBuilder().build();

        Request request = new Request.Builder()
                .url(moviesApiGetFilms)
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + bearerToken)
                .build();

        Response response = client.newCall(request).execute();
        String text = response.body().string();

        if (response.code() != HttpStatus.OK.value()) {
            throw new HttpException(text);
        }

        return text;
    }
}
