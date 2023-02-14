package shop.dodotalk.dorundorun.security.dto;

import shop.dodotalk.dorundorun.security.dto.UserProfile;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {
    GOOGLE("google", (attributes) -> {
        UserProfile userProfile = new UserProfile();

        userProfile.setSocialUid((String) attributes.get("sub"));

        userProfile.setName((String) attributes.get("name"));
        userProfile.setEmail((String) attributes.get("email"));
        userProfile.setProfile((String) attributes.get("picture"));


        return userProfile;
    }),

    NAVER("naver", (attributes) -> {
        // 응답 형식
        // id, nickname, profile_image, age, gender, email, mobile, mobile_e164, name, birthday, birthyear

        // response에 네이버에서 사용자 정보에 대한 응답 데이터가 전달 됨.
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        UserProfile userProfile = new UserProfile();

        userProfile.setSocialUid((String) response.get("id"));

        userProfile.setName((String) response.get("name"));
        userProfile.setNickname((String) response.get("nickname"));

        userProfile.setEmail(((String) response.get("email")));
        userProfile.setProfile((String) response.get("profile_image"));

        userProfile.setAge_range((String) response.get("age"));
        userProfile.setGender((String) response.get("gender"));

        userProfile.setMobile((String) response.get("mobile"));
        userProfile.setMobile_e164((String) response.get("mobile_e164"));

        userProfile.setBirthday((String) response.get("birthday"));
        userProfile.setBirthyear((String) response.get("birthyear"));

        return userProfile;
    }),

    KAKAO("kakao", (attributes) -> {
        // kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        UserProfile userProfile = new UserProfile();

        userProfile.setSocialUid(String.valueOf(attributes.get("id")));

        userProfile.setName((String) kakaoProfile.get("nickname"));
        userProfile.setEmail((String) kakaoAccount.get("email"));

        userProfile.setProfile((String) kakaoProfile.get("profile_image_url"));
        userProfile.setThumbnail((String) kakaoProfile.get("thumbnail_image_url"));

        userProfile.setGender((String) kakaoAccount.get("gender"));
        userProfile.setBirthday((String) kakaoAccount.get("birthday"));
        userProfile.setAge_range((String) kakaoAccount.get("age_range"));
        return userProfile;
    });

    private final String registrationId;
    private final Function<Map<String, Object>, UserProfile> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, UserProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }

}
