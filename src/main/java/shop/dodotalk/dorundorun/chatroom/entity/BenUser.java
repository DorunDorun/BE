package shop.dodotalk.dorundorun.chatroom.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenUser extends Timestamped {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long benUserId;

    @Column
    private String roomId;

    @Column
    private Long userId;
}
