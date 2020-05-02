package valid.movies.web.service;

import valid.movies.web.model.Film;
import valid.movies.web.model.OAuth2AuthenticationToken;

import java.util.List;

public interface MoviesService {

    List<Film> retrieveFilmsInventory(OAuth2AuthenticationToken oauth2AuthenticationToken);
}
