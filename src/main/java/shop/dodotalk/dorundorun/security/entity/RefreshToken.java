package shop.dodotalk.dorundorun.security.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @Column
    private String userEmail;

    @Column(length = 350)
    private String token;


    private RefreshToken(String userEmail, String token) {
        this.userEmail = userEmail;
        this.token = token;
    }

    public static RefreshToken createToken(String userId, String token){
        return new RefreshToken(userId, token);
    }

    public void changeToken(String token) {
        this.token = token;
    }
}
