package shop.dodotalk.dorundorun.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false) // swagger default response message 삭제
                .select()
                .apis(RequestHandlerSelectors.basePackage("shop.dodotalk.dorundorun"))
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }
}