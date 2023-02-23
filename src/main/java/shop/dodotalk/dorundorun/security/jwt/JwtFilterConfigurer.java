package shop.dodotalk.dorundorun.security.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import shop.dodotalk.dorundorun.users.service.UserPrincipalService;


public class JwtFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private JwtUtil jwtUtil;
    private UserPrincipalService userPrincipalService;

    public JwtFilterConfigurer(JwtUtil jwtUtil, UserPrincipalService userPrincipalService) {
        this.jwtUtil = jwtUtil;
        this.userPrincipalService = userPrincipalService;
    }

    @Override
    public void configure(HttpSecurity builder) {
        JwtAuthenticationFilter customFilter = new JwtAuthenticationFilter(jwtUtil, userPrincipalService);

        builder.addFilterAfter(customFilter, LogoutFilter.class);
    }
}
