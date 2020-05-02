package valid.movies.web.stub;

import org.apache.http.HttpException;

import java.io.IOException;

public interface OAuth2AuthServerStub {

    String call(String username, String password) throws IOException, HttpException;
}
