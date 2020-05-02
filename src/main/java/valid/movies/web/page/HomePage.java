package valid.movies.web.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import valid.movies.web.model.Film;
import valid.movies.web.model.OAuth2AuthenticationToken;
import valid.movies.web.service.MoviesService;

import java.util.List;

@Controller
public class HomePage {

    private MoviesService moviesService;

    @Autowired
    public HomePage(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @RequestMapping("/home")
    public String home(Model model) {

        OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        List<Film> films = moviesService.retrieveFilmsInventory(oauth2AuthenticationToken);

        model.addAttribute("films", films);
        return "home";
    }
}
