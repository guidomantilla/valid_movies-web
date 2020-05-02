package valid.movies.web.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class OAuth2AuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String accessToken;

    public OAuth2AuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public OAuth2AuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public OAuth2AuthenticationToken(Object principal, Object credentials, String accessToken, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.accessToken = accessToken;
    }

    public String getAccessToken(){
        return accessToken;
    }
}
