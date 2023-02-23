package shop.dodotalk.dorundorun.security.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import shop.dodotalk.dorundorun.security.jwt.JwtUtil;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import static shop.dodotalk.dorundorun.security.jwt.JwtAuthenticationFilter.AUTHORIZATION_HEADER;
import static shop.dodotalk.dorundorun.security.jwt.JwtAuthenticationFilter.REFRESH_HEADER;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        log.info("소셜 인증 성공--onAuthenticationSuccess() start");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();


        /* JWT Access Token 발급 헤더에 넣던지, 쿠키로 전달해주던지. */
        log.info("access token 발급");
//        response.addHeader(AUTHORIZATION_HEADER, "Bearer-" + jwtUtil.generateAccessToken(authentication));
        Cookie accessTokenCookie = new Cookie(AUTHORIZATION_HEADER,"Bearer-" + jwtUtil.generateAccessToken(authentication));
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        response.addCookie(accessTokenCookie);

        /* JWT Refresh Token 발급 헤더에 넣던지, 쿠키로 전달해주던지. */
        log.info("refresh token 발급");
        Cookie refreshTokenCookie = new Cookie(REFRESH_HEADER,"Bearer-" + jwtUtil.issueRefreshToken(authentication));
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(refreshTokenCookie);


        /* 유저 소셜 정보
        * Json으로 변환하여 쿠키에 담아 프론트 엔드에 전달.
        * utf-8 encoding 하지 않으면 한글, 공백 등 불가. */
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(attributes);

        Cookie userInfoCookie = new Cookie("user_Info",
                URLEncoder.encode(jsonStr,"utf-8"));

        userInfoCookie.setPath("/");
        response.addCookie(userInfoCookie);


        if ("kakao".equals(attributes.get("social"))) {
//            String targetUrl = UriComponentsBuilder.fromUriString("https://dorundorun-blond.vercel.app/kakao")
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/kakao")
                    .queryParam(AUTHORIZATION_HEADER, "Bearer-" + jwtUtil.generateAccessToken(authentication))
                    .queryParam(REFRESH_HEADER, "Bearer-" + jwtUtil.issueRefreshToken(authentication))
                    .queryParam("user_Info", URLEncoder.encode(jsonStr,"utf-8"))
                    .build().encode(StandardCharsets.UTF_8)
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }


        if ("google".equals(attributes.get("social"))) {
//            String targetUrl = UriComponentsBuilder.fromUriString("https://dorundorun-blond.vercel.app/google")
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/google")
                    .queryParam(AUTHORIZATION_HEADER, "Bearer-" + jwtUtil.generateAccessToken(authentication))
                    .queryParam(REFRESH_HEADER, "Bearer-" + jwtUtil.issueRefreshToken(authentication))
                    .queryParam("user_Info", URLEncoder.encode(jsonStr,"utf-8"))
                    .build().encode(StandardCharsets.UTF_8)
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        }



        if ("naver".equals(attributes.get("social"))) {
//            String targetUrl = UriComponentsBuilder.fromUriString("https://dorundorun-blond.vercel.app/naver")
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/naver")
                    .queryParam(AUTHORIZATION_HEADER, "Bearer-" + jwtUtil.generateAccessToken(authentication))
                    .queryParam(REFRESH_HEADER, "Bearer-" + jwtUtil.issueRefreshToken(authentication))
                    .queryParam("user_Info", URLEncoder.encode(jsonStr,"utf-8"))
                    .build().encode(StandardCharsets.UTF_8)
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }






        log.info("onAuthenticationSuccess() end");


        /* 마지막에 프론트 엔드 로그인 완료 화면으로 리다이렉트. */




    }
}
