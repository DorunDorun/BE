package shop.dodotalk.dorundorun.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.dodotalk.dorundorun.chatroom.entity.Category;
import shop.dodotalk.dorundorun.chatroom.entity.Saying;

import java.util.List;


@Repository
public interface SayingRepository extends JpaRepository<Saying, Long> {

    List<Saying> findByCategory(Category category);


}
