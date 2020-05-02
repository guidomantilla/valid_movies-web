package valid.movies.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import valid.movies.web.service.OAuth2AuthService;

@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebMvcSecurityConfig extends WebMvcSecurityConfigAdapter {

    @Autowired
    public WebMvcSecurityConfig(OAuth2AuthService oAuth2AuthService) {
        super(oAuth2AuthService);
    }
}
