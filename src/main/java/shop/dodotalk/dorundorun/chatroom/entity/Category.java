package shop.dodotalk.dorundorun.chatroom.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {

    @Id
    private Long id;


    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CategoryEnum category;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Saying> sayings = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<ChatRoom> rooms = new ArrayList<>();


}
