package valid.movies.web;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;

public class WebMvcSecurityConfigAdapter extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private AuthenticationProvider authenticationProvider;

    public WebMvcSecurityConfigAdapter(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    //----------------------------
    // Init - WebMvcConfigurer
    //----------------------------

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**", "/css/**", "/js/**")
                .addResourceLocations("classpath:/static/img/", "classpath:/static/css/", "classpath:/static/js/");
    }

    //----------------------------
    // End - WebMvcConfigurer
    //----------------------------


    //----------------------------
    // Init - WebSecurityConfigurerAdapter
    //----------------------------

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        turnOnSecurity(http, "/", "/error**", "/js/**", "/css/**");
        sessionBehavior(http);
        errorHandling(http);
    }

    //----------------------------
    // End - WebSecurityConfigurerAdapter
    //----------------------------

    public void turnOnSecurity(HttpSecurity http, String... allowAntPatterns) throws Exception {

        http.httpBasic();
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.cors().disable();

        http.authorizeRequests().antMatchers(allowAntPatterns).permitAll();
        http.authorizeRequests().anyRequest().authenticated();
    }

    public void turnOffSecurity(HttpSecurity http, String... allowAntPatterns) throws Exception {

        http.httpBasic().disable();
        http.csrf().disable();
        http.cors().disable();

        http.authorizeRequests().antMatchers(allowAntPatterns).permitAll();
        http.authorizeRequests().anyRequest().permitAll();
    }

    public void sessionBehavior(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage("/login").defaultSuccessUrl("/home", true).failureUrl("/login-error")
                .and() //.deleteCookies("JSESSIONID", "remember-me")
                .logout().logoutUrl("/logout").clearAuthentication(true).invalidateHttpSession(true).deleteCookies().logoutSuccessUrl("/")
                /*.and()
                .rememberMe()
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                .key("somethingverysecured")*/;

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
    }

    public void errorHandling(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .accessDeniedHandler(
                        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));
    }
}
