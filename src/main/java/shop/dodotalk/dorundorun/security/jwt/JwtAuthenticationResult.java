package shop.dodotalk.dorundorun.security.jwt;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationResult extends OAuth2UserInfoAuthentication {

    public JwtAuthenticationResult(String uid, String provider, String email, Collection<? extends GrantedAuthority> authorities) {
        super(uid, provider, email, authorities);
    }

}