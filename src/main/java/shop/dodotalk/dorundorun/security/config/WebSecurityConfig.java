package shop.dodotalk.dorundorun.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import shop.dodotalk.dorundorun.security.handler.OAuth2SuccessHandler;
import shop.dodotalk.dorundorun.security.service.OAuthService;
import shop.dodotalk.dorundorun.security.jwt.JwtFilterConfigurer;
import shop.dodotalk.dorundorun.security.jwt.JwtUtil;
import shop.dodotalk.dorundorun.users.service.UserPrincipalService;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final OAuthService oAuthService;
    private final OAuth2SuccessHandler successHandler;
    private final JwtUtil jwtUtil;
    private final UserPrincipalService userPrincipalService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /* 대략적인 로직
        * 1. JWT 토큰 검사 -> 성공 시 로그인 성공
        * 2. 실패 시 -> 다음 필터 진행
        * 3. OAuth 인증 로직 진행
        * */

        /* csrf 설정가 해제. */
        http.csrf().disable();


        /* JWT */
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.apply(new JwtFilterConfigurer(jwtUtil, userPrincipalService));


        /* URL Mapping */
        http.authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/").permitAll()

                // todo 테스트 시 원활한 진행용으로 일단 모든 접근 허용
                .anyRequest().permitAll();
                //.anyRequest().authenticated();



        /* Oauth2 */
        http.oauth2Login()
                .successHandler(successHandler)
                .userInfoEndpoint()
                .userService(oAuthService);


        // cors 설정.
        http.cors().configurationSource(corsConfigurationSource());


        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        /* 일단 기본 설정으로 전부 허용.
        * todo 서버 배포 시 프론트엔드 서버로 바꿀 예정 */
//        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("Set-Cookie");

        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;

    }
}
