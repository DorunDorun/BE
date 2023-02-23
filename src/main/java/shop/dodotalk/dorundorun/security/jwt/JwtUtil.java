package shop.dodotalk.dorundorun.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import shop.dodotalk.dorundorun.security.entity.RefreshToken;
import shop.dodotalk.dorundorun.security.repository.RefreshTokenRepository;
import shop.dodotalk.dorundorun.users.repository.UserRepository;
import shop.dodotalk.dorundorun.security.service.OAuthService;
import shop.dodotalk.dorundorun.users.service.UserPrincipalService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil implements InitializingBean {


    /* 시크릿 키 */
    private final String secretKey;

    /* Access Token 만료기간 */
    private final long tokenValidityInMs;

    /* Refresh Token 만료기간 */
    private final long refreshTokenValidityInMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuthService oAuthService;

    private final UserRepository userRepository;
    private final UserPrincipalService userPrincipalService;


    public JwtUtil(@Value("${jwt.secret-key}") String secretKey,
                   @Value("${jwt.token-validity-in-sec}") long tokenValidity,
                   @Value("${jwt.refresh-token-validity-in-sec}") long refreshTokenValidity,
                   RefreshTokenRepository refreshTokenRepository,
                   OAuthService oAuthService,
                   UserRepository userRepository,
                   UserPrincipalService userPrincipalService) {

        this.secretKey = secretKey;
        this.tokenValidityInMs = tokenValidity * 1000;
        this.refreshTokenValidityInMs = refreshTokenValidity * 1000;
        this.refreshTokenRepository = refreshTokenRepository;
        this.oAuthService = oAuthService;
        this.userRepository = userRepository;
        this.userPrincipalService = userPrincipalService;
    }


    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        key = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }


    public static enum JwtCode {
        DENIED,
        ACCESS,
        EXPIRED;
    }


    /* 액세스 토큰 생성 */
    public String generateAccessToken(Authentication authentication) {
        log.info("generate Access Token ...");

        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMs);

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();

            String uid = (String) attributes.get("id");
            String email = (String) attributes.get("email");
            String provider = (String) attributes.get("social");


            return Jwts.builder()
                    .setSubject(uid)
                    .claim("provider", provider)
                    .claim("email", email)
                    .claim("authorities", authorities)
                    .setIssuedAt(now) // 발행시간
                    .signWith(key, SignatureAlgorithm.HS512) // 암호화
                    .setExpiration(validity) // 만료
                    .compact();
        }

        if (authentication instanceof JwtAuthenticationResult) {
            String uid = ((JwtAuthenticationResult) authentication).getUid();
            String email = ((JwtAuthenticationResult) authentication).getEmail();
            String provider = ((JwtAuthenticationResult) authentication).getProvider();

            return Jwts.builder()
                    .setSubject(uid)
                    .claim("provider", provider)
                    .claim("email", email)
                    .claim("authorities", authorities)
                    .setIssuedAt(now) // 발행시간
                    .signWith(key, SignatureAlgorithm.HS512) // 암호화
                    .setExpiration(validity) // 만료
                    .compact();



        }


        return null;

    }

    /* Access 토큰의 유효성을 검증한다.*/
    public JwtCode validateToken(String token) {
        try {
            log.info("validate Access Token ...");

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            log.info("JWT 토큰 검증 완료");
            return JwtCode.ACCESS;
        } catch (ExpiredJwtException e) {
            // Access Token이 만료된 경우에는
            // refresh token을 확인하기 위해 EXPIRED 리턴
            log.info("JWT 토큰 만료됨");


            return JwtCode.EXPIRED;

        } catch (JwtException | IllegalArgumentException e) {
            log.info("jwtException : {}", e);
        }
        return JwtCode.DENIED;
    }


    public JwtCode validateRefreshToken(String refreshToken) {
        try {
            log.info("validate Refresh Token ...");

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);
            log.info("JWT 토큰 검증 완료");
            log.info("JWT 토큰 DB 대조 시작");

            Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(refreshToken);

            if (refreshTokenOptional.isEmpty()) {
                throw new IllegalArgumentException("Refresh Token이 DB에 존재하지 않음.");
            }


            return JwtCode.ACCESS;
        } catch (ExpiredJwtException e) {
            // Access Token이 만료된 경우에는
            // refresh token을 확인하기 위해 EXPIRED 리턴
            log.info("JWT 토큰 만료됨");


            return JwtCode.EXPIRED;

        } catch (JwtException | IllegalArgumentException e) {
            log.info("jwtException : {}", e);
        }
        return JwtCode.DENIED;
    }


    /* 리프레쉬 토큰 생성 */
    public String generateRefreshToken(Authentication authentication) {
        log.info("generate Refresh Token ...");
        /* 오어쓰 로그인 시 리프레쉬 토큰 생성할 때랑
         * JWT 토큰을 듣고 올때랑 타입이 달라서
         * JWT 토큰을 들고 올때는 OAuth2User로 변환이 안됨
         * 타입 확인 후 JWT... 타입이면 바로 진행*/


        /*org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
         * shop.dodotalk.dorundorun.security.jwt.JwtAuthenticationResult*/


        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());


        Map<String, Object> claims = new HashMap<>();


        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityInMs);

        /* OAuth2 타입인 경우 */
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();

            String uid = (String) attributes.get("id");
            String email = (String) attributes.get("email");
            String provider = (String) attributes.get("social");

            return Jwts.builder()
                    .setSubject(uid)
                    .claim("provider", provider)
                    .claim("email", email)
                    .claim("authorities", authorities)
                    .setIssuedAt(now) // 발행시간
                    .signWith(key, SignatureAlgorithm.HS512) // 암호화
                    .setExpiration(validity) // 만료
                    .compact();
        }

        /* Jwt 타입인 경우 */
        if (authentication instanceof JwtAuthenticationResult) {

            String uid = ((JwtAuthenticationResult) authentication).getUid();
            String email = ((JwtAuthenticationResult) authentication).getEmail();
            String provider = ((JwtAuthenticationResult) authentication).getProvider();


            return Jwts.builder()
                    .setSubject(uid)
                    .claim("provider", provider)
                    .claim("email", email)
                    .claim("authorities", authorities)
                    .setIssuedAt(now) // 발행시간
                    .signWith(key, SignatureAlgorithm.HS512) // 암호화
                    .setExpiration(validity) // 만료
                    .compact();
        }

        return null;
    }


    @Transactional
    public String issueRefreshToken(Authentication authentication) {

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String newRefreshToken = generateRefreshToken(authentication);


        // 기존것이 있다면 바꿔주고, 없다면 만들어줌
        refreshTokenRepository.findByUserEmail((String) oAuth2User.getAttributes().get("email"))
                .ifPresentOrElse(
                        r -> {
                            r.changeToken(newRefreshToken);
                            log.info("issueRefreshToken method | change token ");
                        },
                        () -> {
                            RefreshToken token = RefreshToken.createToken((String) oAuth2User.getAttributes().get("email"), newRefreshToken);
                            log.info(" issueRefreshToken method | save tokenID : {}, token : {}", token.getUserEmail(), token.getToken());
                            refreshTokenRepository.save(token);
                        });

        return newRefreshToken;
    }


    /* Refresh Token 재발급
     * Refresh Token이 만료된 경우. */
    @Transactional
    public String reissueRefreshToken(String refreshToken) throws RuntimeException {

        // Refresh Token 검증 -> 해당 유저의 정보를 가지고 온다.
        JwtAuthenticationResult authentication = (JwtAuthenticationResult) getAuthentication(refreshToken);
        Object principal = userPrincipalService.loadUserPrincipal(authentication);
        authentication.setPrincipal(principal);


        String email = authentication.getEmail();



        // 해당 유저 정보와 맞는 Refresh Token이 DB에 있는지 확인.
        RefreshToken findRefreshToken = refreshTokenRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("userEmail : " + email + " was not found"));

        // DB에 있다면 꺼낸 Refresh Token이 클라이언트가 요청한 Refresh Token과 같은지 한번 더 확인.
        if (findRefreshToken.getToken().equals(refreshToken)) {

            // 새로운 RefreshToken 발급
            String newRefreshToken = generateRefreshToken(authentication);

            // 새로운 RefreshToken 업데이트.
            findRefreshToken.changeToken(newRefreshToken);
            return newRefreshToken;
        } else {
            log.info("refresh 토큰이 일치하지 않습니다. ");
            return null;
        }
    }


    /* JWT Token에서 값을 꺼내오는 유틸 */
    public String resolveToken(HttpServletRequest request, String header) {

        log.info("resolve Token ...");

        String bearerToken = request.getHeader(header);

        if (bearerToken != null && bearerToken.startsWith("Bearer-")) {
            // Bearer- 제외 토큰 값만 리턴
            return bearerToken.substring(7);
        }

        // 토큰이 없다면 null 리턴
        return null;
    }


    /* Token을 검증 후 클라이언트의 정보를 가지고 온다. */
    public JwtAuthenticationResult getAuthentication(String token) {
        log.info("get Authentication ...");

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();


        String uid = claims.getSubject();
        String provider = claims.get("provider", String.class);
        String email = claims.get("email", String.class);

        List<? extends GrantedAuthority> grantedAuthorities =
                (List<SimpleGrantedAuthority>) claims.get("authorities", List.class).stream()
                        .map(authority -> new SimpleGrantedAuthority((String) authority))
                        .collect(Collectors.toList());

        return new JwtAuthenticationResult(uid, provider, email, grantedAuthorities);
    }

    /* Socket Access 토큰의 유효성을 검증한다.*/
    public String socketResolveToken(String bearerToken) {
        log.info("resolve Token ...");

        if (bearerToken != null && bearerToken.startsWith("Bearer-")) {
            // Bearer- 제외 토큰 값만 리턴
            return bearerToken.substring(7);
        }

        // 토큰이 없다면 null 리턴
        return null;
    }

}
