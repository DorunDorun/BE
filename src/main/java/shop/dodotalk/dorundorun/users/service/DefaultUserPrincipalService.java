package shop.dodotalk.dorundorun.users.service;

import org.springframework.security.core.Authentication;
import shop.dodotalk.dorundorun.users.entity.User;

public class DefaultUserPrincipalService implements UserPrincipalService {

    @Override
    public User loadUserPrincipal(Authentication authentication) {
        return null;
    }
}
