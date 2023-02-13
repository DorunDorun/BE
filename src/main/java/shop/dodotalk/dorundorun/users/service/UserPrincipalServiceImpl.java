package shop.dodotalk.dorundorun.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.dodotalk.dorundorun.users.entity.User;
import shop.dodotalk.dorundorun.security.jwt.OAuth2UserInfoAuthentication;
import shop.dodotalk.dorundorun.users.repository.UserRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPrincipalServiceImpl implements UserPrincipalService {

    private final UserRepository userRepository;

    /**

     * */
    @Transactional
    @Override
    public Object loadUserPrincipal(Authentication authentication) {

        OAuth2UserInfoAuthentication oauth2UserInfoToken = (OAuth2UserInfoAuthentication) authentication;

        String provider = oauth2UserInfoToken.getProvider();
        String email = oauth2UserInfoToken.getEmail();

        Optional<User> userOptional = userRepository.findByEmailAndProvider(email, provider);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }

        User user = userOptional.get();

        return user;

    }

}
