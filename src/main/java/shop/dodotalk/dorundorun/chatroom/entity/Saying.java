package shop.dodotalk.dorundorun.chatroom.entity;


import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Getter
public class Saying {

    @Id
    private Long id;

    @Column
    private String saying;



    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;


}
