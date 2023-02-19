package shop.dodotalk.dorundorun.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false) // swagger default response message 삭제
                .select()
                .apis(RequestHandlerSelectors.basePackage("shop.dodotalk.dorundorun"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Test API 명세서")
                .description("Test의 API명세서.")
                .version("1.0")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }

//    private ApiInfo apiInfo() {
//        return new ApiInfo(
//                "Sample REST API",
//                "This is sample API.",
//                "V1",
//                "Terms of service",
//                new Contact("administrator", "www.example.com", "administrator@email.com"),
//                "License of API", "www.example.com", Collections.emptyList());
//    }

    //@Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30)
//                .useDefaultResponseMessages(false) // swagger default response message 삭제
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("shop.dodotalk.dorundorun"))
//                .paths(PathSelectors.ant("/api/**"))
//                .build()
//                .apiInfo(apiInfo());
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("WithTopia API 명세서")
//                .description("WITHTOPIA의 API명세서.")
//                .version("1.0")
//                .build();
//    }

//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfo(
//                "TEST API",
//                "Some custom description of API.",
//                "0.0.1",
//                "Terms of service",
//                new Contact("MemoStack", "https://memostack.tistory.com", "public.devhong@gmail.com"),
//                "License of API", "API license URL", Collections.emptyList());
//    }

}