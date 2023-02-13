package shop.dodotalk.dorundorun.users.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자 만들어줌
@DynamicUpdate //update 할때 실제 값이 변경됨 컬럼으로만 update 쿼리를 만듬
@Entity //JPA Entity 임을 명시
@Getter //Lombok 어노테이션으로 getter
@Table(name = "users") //테이블 관련 설정 어노테이션
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column
    private String socialUid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "nickname", nullable = true, unique = true)
    private String nickname;

    @Column
    private String profile;

    @Column
    private String gender;

    @Column
    private String birthday;

    @Column
    private String birthyear;

    @Column
    private String age_range;

    @Column
    private String thumbnail;

    @Column
    private String mobile;

    @Column
    private String mobile_e164;



    @Builder //생성을 Builder 패턴으로 하기 위해서
    public User(Long id, String socialUid, String name, String email, String provider,
                String nickname, String profile, String gender,
                String birthday, String birthyear, String age_range, String thumbnail,
                String mobile, String mobile_e164) {
        this.id = id;
        this.socialUid = socialUid;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.nickname = nickname;
        this.profile = profile;
        this.gender = gender;
        this.birthday = birthday;
        this.birthyear = birthyear;
        this.age_range = age_range;
        this.thumbnail = thumbnail;
        this.mobile = mobile;
        this.mobile_e164 = mobile_e164;

    }

    public User update(String name, String socialUid, String email, String provider,
                       String nickname, String profile, String gender,
                       String birthday, String birthyear, String age_range, String thumbnail_image_url,
                       String mobile, String mobile_e164) {
        this.socialUid = socialUid;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.nickname = nickname;
        this.profile = profile;
        this.gender = gender;
        this.birthday = birthday;
        this.age_range = age_range;
        this.thumbnail = thumbnail_image_url;
        this.mobile = mobile;
        this.mobile_e164 = mobile_e164;
        return this;
    }



}