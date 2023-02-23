package shop.dodotalk.dorundorun.chatroom.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shop.dodotalk.dorundorun.chatroom.entity.Category;
import shop.dodotalk.dorundorun.chatroom.entity.CategoryEnum;
import shop.dodotalk.dorundorun.chatroom.entity.Saying;
import shop.dodotalk.dorundorun.chatroom.repository.CategoryRepository;
import shop.dodotalk.dorundorun.chatroom.repository.SayingRepository;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Component
public class CreateSaying {


    private final CategoryRepository categoryRepository;

    public String createSaying(CategoryEnum category) {


        Category categoryFind = categoryRepository.findByCategory(category).orElseThrow(
                ()-> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다.")
        );

        List<Saying> sayingList = categoryFind.getSayings();

        int sayingSize = sayingList.size();
        int randomInt = (int) ((Math.random() * 10000) % sayingSize);


        Saying saying = sayingList.get(randomInt);


        return saying.getSaying();

    }


}
