package shop.dodotalk.dorundorun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DorunDorunApplication {

    public static void main(String[] args) {
        SpringApplication.run(DorunDorunApplication.class, args);
    }

}
