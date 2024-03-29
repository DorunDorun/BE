package shop.dodotalk.dorundorun.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;


import org.apache.commons.lang3.StringUtils;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.filter.OncePerRequestFilter;
import shop.dodotalk.dorundorun.error.ExceptionResponseMessage;

import shop.dodotalk.dorundorun.users.entity.User;
import shop.dodotalk.dorundorun.users.service.UserPrincipalService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.security.URIParameter;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";

    private UserPrincipalService userPrincipalService;
    private JwtUtil jwtUtil;


    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   UserPrincipalService userPrincipalService) {
        this.jwtUtil = jwtUtil;
        this.userPrincipalService = userPrincipalService;
    }


    /* 무조건 실행되며,
     * - JWT 토큰 성공 시 로그인
     * - JWT 토큰 실패 시 OAuth2 인증과정을 거친다. */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("Jwt Filter ready");

        String requestURI = request.getRequestURI();
        log.info("사용자가 요청한 URI" + requestURI);

        /*JWT 필터를 탈 필요가 없는 것들*/
        if (StringUtils.startsWithAny(requestURI,
                "/oauth2", "/login", "/oauth", "/ws-stomp", "/api/sse", "/actuator", "/api/ssehtml", "/api/count",
                "/api/rooms/info", "/test/") ||
                requestURI.equals("/")) {

            log.info("요청URI : " + requestURI + "----JWT 필터 안 거침");

            filterChain.doFilter(request, response);
            return;
        }

        log.info("Jwt Filter start");


        String body = null;
        JSONObject jsonObject = null;
        String jwtAccessToken = null;
        String jwtRefreshToken = null;

        if (StringUtils.endsWithAny(requestURI, "/delete")) {

            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;

            try {
                InputStream inputStream = request.getInputStream();
                if (inputStream != null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    char[] charBuffer = new char[128];
                    int bytesRead = -1;
                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                        stringBuilder.append(charBuffer, 0, bytesRead);
                    }
                }
            } catch (IOException ex) {
                throw ex;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        throw ex;
                    }
                }
            }

            body = stringBuilder.toString();
            JSONParser jsonParser = new JSONParser();

            log.info(body);
            try {
                jsonObject = (JSONObject) jsonParser.parse(body);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


            if (jsonObject != null) {

                jwtAccessToken = ((String) jsonObject.get("authorization")).substring(7);
                jwtRefreshToken = ((String) jsonObject.get("refresh")).substring(7);

                log.info(jwtAccessToken);
                log.info(jwtRefreshToken);
            }

        }



        /* resolveToken()을 통해 request Header에서 토큰값을 빼온다. */
        if(jwtAccessToken == null) jwtAccessToken = jwtUtil.resolveToken(request, AUTHORIZATION_HEADER);

        log.info("Access Token : " + jwtAccessToken);


        log.info("request가 살아있나? " + request);
        /* Access Token 검증 성공인 경우 */
        if (jwtAccessToken != null && jwtUtil.validateToken(jwtAccessToken) == JwtUtil.JwtCode.ACCESS) {

            /* Access Token이 유효하다면 토큰에서 사용자의 정보를 만들어 authentication으로 빼온다. */
            JwtAuthenticationResult authentication = (JwtAuthenticationResult) jwtUtil.getAuthentication(jwtAccessToken);

            /* 위에서 만든 정보를 토대로 userRepository에서 User 객체(유저정보)를 빼온다. */
            User principal = userPrincipalService.loadUserPrincipal(authentication);

            /* 인증 객체에 DB에 있던 User정보 셋팅 */
            authentication.setPrincipal(principal);

            /* 인증 성공 처리 --> 인증 성공으로, 다음 필터 진행 안함 */
            successfulAuthentication(request, response, filterChain, authentication);

        }

        /* Access 토큰이 만료된 경우 */
        else if (jwtAccessToken != null && jwtUtil.validateToken(jwtAccessToken) == JwtUtil.JwtCode.EXPIRED) {
            log.info("JWT 토큰이 만료되어, Refresh token 확인 작업을 진행합니다.");

            /* Refresh Token 존재 여부 확인.*/
            if(jwtRefreshToken == null) jwtRefreshToken = jwtUtil.resolveToken(request, REFRESH_HEADER);


            /* Refresh Token이 검증이 되며, 만료기간이 지나지 않은 경우만 */
            if (jwtRefreshToken != null && jwtUtil.validateRefreshToken(jwtRefreshToken) == JwtUtil.JwtCode.ACCESS) {

                String newJwtRefreshToken = jwtUtil.reissueRefreshToken(jwtRefreshToken);

                // 재발급이 이상없이 됐다면.
                if (newJwtRefreshToken != null) {

                    /* Refresh Token에서 정보를 가지고와 인증 객체를 만든다. */
                    JwtAuthenticationResult authentication = (JwtAuthenticationResult) jwtUtil.getAuthentication(jwtRefreshToken);

                    /* 위에서 뽑아온 인증객체로 Access Token 재발급 한다. */
                    response.setHeader(AUTHORIZATION_HEADER, "Bearer-" + jwtUtil.generateAccessToken(authentication));

                    /* Access Token 재발급에 이상이 없다면 헤더에 NewRefreshToken도 같이 담아 보냄. */
                    response.setHeader(REFRESH_HEADER, "Bearer-" + newJwtRefreshToken);


                    /* 로그인 성공 처리를 한다. */
                    Object principal = userPrincipalService.loadUserPrincipal(authentication);
                    authentication.setPrincipal(principal);

                    /* 인증 성공 */
                    successfulAuthentication(request, response, filterChain, authentication);


                    log.info("reissue refresh Token & access Token");
                }
            } else {

                log.info("no valid JWT Refresh token found, uri: {}", request.getRequestURI());

                try (OutputStream os = response.getOutputStream()) {


                    ExceptionResponseMessage exceptionDto =
                            new ExceptionResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Refresh Token이 유효하지 않거나, 만료기간이 지났습니다.");

                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.writeValue(os, exceptionDto);
                    os.flush();
                    return;
                }
            }


        } else { /*Access Token이 유효하지 않은 경우 -> 로그인할 때 제외.*/

            log.info("no valid JWT Access token found, uri: {}", request.getRequestURI());

            try (OutputStream os = response.getOutputStream()) {

                ExceptionResponseMessage exceptionDto =
                        new ExceptionResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Access Token이 유효하지 않습니다.");

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(os, exceptionDto);
                os.flush();
                return;
            }


        }

        /* Access Token이 없거나 검증이 안되는 경우
         * Refresh Token이 만료된 경우 등은 로그인 처리 하지 않고 다음 필터로 진행 */
        filterChain.doFilter(request, response);
    }


    /* 토큰 인증 성공 시 */
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("jwt token authentication success!");

        authResult.setAuthenticated(true);
        ((JwtAuthenticationResult) authResult).setDetails(request.getRemoteAddr());
        SecurityContextHolder.getContext().setAuthentication(authResult);

        log.info(String.valueOf(authResult));
        log.info(SecurityContextHolder.getContext().toString());


    }


}
