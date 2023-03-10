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
@EnableWebSecurity(debug = false)
public class WebSecurityConfig {

    private final OAuthService oAuthService;
    private final OAuth2SuccessHandler successHandler;
    private final JwtUtil jwtUtil;
    private final UserPrincipalService userPrincipalService;

    private static final String[] PERMIT_URL_ARRAY = {
        /* swagger */
        "/v2/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**",
        "/v3/api-docs/**",
        "/swagger-ui/**"
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /* ???????????? ??????
        * 1. JWT ?????? ?????? -> ?????? ??? ????????? ??????
        * 2. ?????? ??? -> ?????? ?????? ??????
        * 3. OAuth ?????? ?????? ??????
        * */

        /* csrf ????????? ??????. */
        http.csrf().disable();


        /* JWT */
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.apply(new JwtFilterConfigurer(jwtUtil, userPrincipalService));

        /* URL Mapping */
        http.authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers(PERMIT_URL_ARRAY).permitAll()
                .antMatchers("/ws-stomp/**").permitAll()
                .antMatchers("/ws-stomp").permitAll()
                .antMatchers("/api/sse").permitAll()
                .antMatchers("/api/ssehtml").permitAll()
                .antMatchers("/actuator").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/api/rooms/info").permitAll()
                .antMatchers("/api/ssehtml").permitAll()
                .antMatchers("/api/count").permitAll()
                .anyRequest().authenticated();



        /* Oauth2 */
        http.oauth2Login()
                .successHandler(successHandler)
                .failureUrl("https://dorun-dorun.vercel.app/Login")/*OAuth ????????? ??? ????????? ????????? URL??? ??????????????? ??????*/
                .userInfoEndpoint()
                .userService(oAuthService);


        /* cors ??????. */
        http.cors().configurationSource(corsConfigurationSource());


        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();


//        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("https://dorun-dorun.vercel.app");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("Set-Cookie");
        configuration.addExposedHeader("authorization");
        configuration.addExposedHeader("refresh");

        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;

    }
}
