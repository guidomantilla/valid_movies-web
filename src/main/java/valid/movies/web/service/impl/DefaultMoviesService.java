package valid.movies.web.service.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import valid.movies.web.model.Film;
import valid.movies.web.model.OAuth2AuthenticationToken;
import valid.movies.web.service.MoviesService;
import valid.movies.web.stub.MoviesApiStub;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultMoviesService implements MoviesService {

    private static final Logger logger = LoggerFactory.getLogger(MoviesService.class);

    private ObjectMapper mapper;
    private MoviesApiStub moviesApiStub;

    @Autowired
    public DefaultMoviesService(MoviesApiStub moviesApiStub) {
        this.moviesApiStub = moviesApiStub;
        this.mapper = new ObjectMapper();
    }

    public List<Film> retrieveFilmsInventory(OAuth2AuthenticationToken oauth2AuthenticationToken) {

        String accessToken = oauth2AuthenticationToken.getAccessToken();

        Optional<String> jsonOptional = callMoviesGetFilms(accessToken);
        String json = jsonOptional.orElseThrow(() -> new IllegalStateException("Error en la Comunicacion con el servidor de Movies"));

        Optional<List<Film>> optionalFilms = parseJson(json);
        return optionalFilms.orElse(new ArrayList<Film>());
    }

    private Optional<List<Film>> parseJson(String json) {

        try {

            JsonNode rootNode = mapper.readTree(json);
            JsonNode filmsesNode = rootNode.path("_embedded").path("filmses");

            filmsesNode.forEach(filmNode -> {
                if (filmNode instanceof ObjectNode) {
                    ObjectNode object = (ObjectNode) filmNode;
                    object.remove("_links");
                    object.remove("films");
                }
            });

            StringWriter stringWriter = new StringWriter();
            mapper.writeTree(new JsonFactory().createGenerator(stringWriter), filmsesNode);

            List<Film> films = mapper.readValue(stringWriter.toString(), new TypeReference<List<Film>>() {
            });

            return Optional.of(films);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return Optional.empty();
    }

    private Optional<String> callMoviesGetFilms(String bearerToken) {

        try {

            return Optional.of(moviesApiStub.getFilms(bearerToken));

        } catch (IOException | HttpException e) {
            logger.error(e.getMessage(), e);
        }

        return Optional.empty();
    }
}
