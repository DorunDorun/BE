package shop.dodotalk.dorundorun.swagger.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false) // swagger default response message 삭제
                .securityContexts(securityContext()) // swagger에서 jwt 토큰값 넣기위한 설정
                .securitySchemes(apiKey()) // swagger에서 jwt 토큰값 넣기위한 설정
                .select()
                .apis(RequestHandlerSelectors.basePackage("shop.dodotalk.dorundorun"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("DoRun")
                .description("DorunDorun의 API명세서.")
                .version("1.0")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }

    private List<SecurityScheme> apiKey() {
        List<SecurityScheme> apiKeyList = new ArrayList<>();
        apiKeyList.add(new ApiKey("Access", "Authorization", "header"));
        apiKeyList.add(new ApiKey("Refresh", "Refresh", "header"));
        return apiKeyList;
    }

    private List<SecurityContext> securityContext() {
        List<SecurityContext> contextList = new ArrayList<>();
        contextList.add(SecurityContext.builder().securityReferences(defaultAuth("Access")).build());
        contextList.add(SecurityContext.builder().securityReferences(defaultAuth("Refresh")).build());
        return contextList;
    }

    private List<SecurityReference> defaultAuth(String apiKey) {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference(apiKey, authorizationScopes));
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
        super.configureMessageConverters(converters);
    }
}