package valid.movies.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import valid.movies.web.service.OAuth2AuthenticationProvider;

import javax.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private OAuth2AuthenticationProvider oAuth2AuthenticationProvider;

    @Autowired
    public SecurityConfig(OAuth2AuthenticationProvider oAuth2AuthenticationProvider) {
        this.oAuth2AuthenticationProvider = oAuth2AuthenticationProvider;
    }

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(oAuth2AuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.httpBasic();
        http.cors().disable();
        http.csrf().disable();

        http.authorizeRequests().antMatchers("/", "/error**", "/js/**", "/css/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();

        http.formLogin().loginPage("/login").defaultSuccessUrl("/home").failureUrl("/login-error")
        .and().logout().logoutUrl("/logout").logoutSuccessUrl("/");

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .accessDeniedHandler(
                        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));
    }
}
