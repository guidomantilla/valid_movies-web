package valid.movies.web.stub;

import org.apache.http.HttpException;

import java.io.IOException;

public interface MoviesApiStub {

    String getFilms(String bearerToken) throws IOException, HttpException;
}
