package shop.dodotalk.dorundorun.users.service;

import org.springframework.security.core.Authentication;

public class DefaultUserPrincipalService implements UserPrincipalService {

    @Override
    public Object loadUserPrincipal(Authentication authentication) {
        return null;
    }
}
