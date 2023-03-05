package shop.dodotalk.dorundorun.chatroom.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import shop.dodotalk.dorundorun.users.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends Timestamped {

    @Id
    private String sessionId;      // 방 번호
    // Openvidu에서 발급된 해당 채팅방에 입장하기 위한 세션 (세션 == 채팅방)
    // 다른 유저들이 해당 채팅방에 입장 요청시 해당 컬럼을 사용하여 오픈비두에 다른 유저들의 채팅방 입장을 위한 토큰을 생성합니다.

    @Column(nullable = false)
    private String title;       // 방 제목

    @Column
    private String subtitle;    // 방 부제목 (내용)

    @Column
    private boolean status;      // 방 상태(public / private)



    @Column
    private String password;    // private시 사용할 패스워드



    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;  // 방의 카테고리.

    @Column
    private String master;         // 방 생성자(방장)

    @Column
    private Long masterUserId;            // 방 생성자 유저 고유번호.



    @Where(clause = "is_delete = false")
    @OneToMany(mappedBy = "sessionId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomUser> chatRoomUserList;

    @Column
    private Long cntUser;         // 현재 방 인원

    @Column
    private boolean isDelete;   //  방 삭제 여부.

    @Column
    private LocalDateTime roomDeleteTime;

    public void deleteRoom (LocalDateTime roomDeleteTime) {
        this.isDelete = true;
        this.roomDeleteTime = roomDeleteTime;
    }

    public void rename(String updateTitle){
        this.title = updateTitle;
    }

    public void updateCntUser(Long cntUser) {
        this.cntUser = cntUser;
    }

    public boolean validateUser(User user) {
        return !this.master.equals(user.getSocialUid());
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }
}