package shop.dodotalk.dorundorun.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import shop.dodotalk.dorundorun.security.dto.OAuthAttributes;
import shop.dodotalk.dorundorun.users.entity.User;
import shop.dodotalk.dorundorun.security.dto.UserProfile;
import shop.dodotalk.dorundorun.users.repository.UserRepository;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/*
    OAuth2 로그인 성공시 DB에 저장하는 작업
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser(OAuth2UserRequest userRequest) start");


        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // OAuth 서비스(kakao, google, naver)에서 가져온 유저 정보를 담고있음

        String registrationId = userRequest.getClientRegistration()
                .getRegistrationId(); // OAuth 서비스 이름(ex. kakao, naver, google)

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName(); // OAuth 로그인 시 키(pk)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); // OAuth 서비스의 유저 정보들


        UserProfile userProfile = OAuthAttributes.extract(registrationId, attributes); // registrationId에 따라 유저 정보를 통해 공통된 UserProfile 객체로 만들어 줌
        userProfile.setProvider(registrationId);


        User user = saveOrUpdate(userProfile);

        Map<String, Object> customAttribute = customAttribute(attributes, userNameAttributeName, userProfile, registrationId);


        log.info("loadUser(OAuth2UserRequest userRequest) end");
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                customAttribute,
                userNameAttributeName);

    }

    private Map customAttribute(Map attributes, String userNameAttributeName, UserProfile userProfile, String registrationId) {
        Map<String, Object> customAttribute = new LinkedHashMap<>();
        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));

        customAttribute.put("id", userProfile.getSocialUid());
        customAttribute.put("email", userProfile.getEmail());
        customAttribute.put("name", userProfile.getName());
        customAttribute.put("social", userProfile.getProvider());
        customAttribute.put("profile", userProfile.getProfile());
        customAttribute.put("thumbnail_image_url", userProfile.getThumbnail());
        customAttribute.put("gender", userProfile.getGender());
        customAttribute.put("birthday", userProfile.getBirthday());
        customAttribute.put("age_range", userProfile.getAge_range());


        return customAttribute;

    }


    // OAuth 서비스 사이트에서 유저 정보 변경이 있을 수 있기 때문에 우리 DB에도 update
    private User saveOrUpdate(UserProfile userProfile) {

        User user = userRepository.findByEmailAndProvider(userProfile.getEmail(), userProfile.getProvider())
                .map(m -> m.update(userProfile.getSocialUid(), userProfile.getName(), userProfile.getEmail(),
                        userProfile.getProvider(), userProfile.getNickname(),
                        userProfile.getProfile(), userProfile.getGender(), userProfile.getBirthday(), userProfile.getBirthyear(),
                        userProfile.getAge_range(), userProfile.getThumbnail(),
                        userProfile.getMobile(), userProfile.getMobile_e164()))
                .orElse(userProfile.toUser());

        return userRepository.save(user);
    }


    /* DB에서 유저 정보 조회 */
    public User loadByUserEmail(String email, String provider) {

        Optional<User> userOptional = userRepository.findByEmailAndProvider(email, provider);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("토큰위조???? / 존재하지 않는 유저");
        }

        User user = userOptional.get();


        return user;
    }

}



