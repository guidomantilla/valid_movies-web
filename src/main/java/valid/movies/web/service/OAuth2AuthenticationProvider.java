package valid.movies.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;
import valid.movies.web.stub.OAuth2AuthenticationStub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OAuth2UserDetailsService implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2UserDetailsService.class);

    private OAuth2AuthenticationStub oAuth2AuthenticationStub;
    private ObjectMapper mapper;

    @Autowired
    public OAuth2UserDetailsService(OAuth2AuthenticationStub oAuth2AuthenticationStub) {
        this.oAuth2AuthenticationStub = oAuth2AuthenticationStub;
        this.mapper = new ObjectMapper();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<String> responseOptional = callOAuth2TokenGenerator(username, password);
        String response = responseOptional.orElseThrow(() -> new AuthenticationServiceException("Error en la Comunicacion con el servidor de Authenticacion"));

        Optional<String> tokenOptional = extractToken(response);
        String token = tokenOptional.orElseThrow(() -> new AuthenticationServiceException("Error en la Extrayendo el token"));

        Optional<List<GrantedAuthority>> optionalGrantedAuthorities = extractAuthorities(token);
        List<GrantedAuthority> grantedAuthorities = optionalGrantedAuthorities.orElseThrow(() -> new AuthenticationServiceException("Error en la Extrayendo los permisos del token"));

        Authentication auth = new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
        return auth;
    }

    private Optional<List<GrantedAuthority>> extractAuthorities(String token) {

        try {

            Jwt jwt = JwtHelper.decode(token);
            Map<String, Object> claims = mapper.readValue(jwt.getClaims(), Map.class);
            List<String> authoritiesTemp = (List<String>) claims.get("authorities");

            List<GrantedAuthority> authorities = new ArrayList<>();
            authoritiesTemp.forEach(string -> authorities.add(new SimpleGrantedAuthority(string)));

            return Optional.of(authorities);

        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }

        return Optional.empty();
    }

    private Optional<String> extractToken(String response) {

        try {

            JsonNode node = mapper.readTree(response);
            String token = node.path("access_token").asText();

            return Optional.of(token);

        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }

        return Optional.empty();
    }

    private Optional<String> callOAuth2TokenGenerator(String username, String password) {

        try {

            return Optional.of(oAuth2AuthenticationStub.call(username, password));

        } catch (IOException | HttpException e) {
            logger.error(e.getMessage(), e);
        }

        return Optional.empty();
    }
}