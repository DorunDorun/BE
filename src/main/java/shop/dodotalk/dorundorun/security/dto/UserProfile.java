package shop.dodotalk.dorundorun.security.dto;

import lombok.Getter;
import lombok.Setter;
import shop.dodotalk.dorundorun.users.entity.User;

@Getter
@Setter
public class UserProfile {
    private String name;
    private String email;
    private String profile;
    private String provider;
    private String nickname;
    private String gender;
    private String birthday;
    private String birthyear;
    private String age_range;
    private String thumbnail;
    private String mobile;
    private String mobile_e164;

    public User toUser() {
        return User.builder()
                .name(name)
                .email(email)
                .profile(profile)
                .provider(provider)
                .nickname(nickname)
                .gender(gender)
                .birthday(birthday)
                .birthyear(birthyear)
                .age_range(age_range)
                .thumbnail(thumbnail)
                .mobile(mobile)
                .mobile_e164(mobile_e164)
                .build();
    }



}
