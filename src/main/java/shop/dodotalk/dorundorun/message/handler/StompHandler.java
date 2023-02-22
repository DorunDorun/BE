package shop.dodotalk.dorundorun.message.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import shop.dodotalk.dorundorun.error.CustomErrorException;
import shop.dodotalk.dorundorun.security.jwt.JwtUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final JwtUtil jwtUtil;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String accessToken = "";
        String refreshToken = "";

        if(accessor.getCommand() == StompCommand.CONNECT) {
            System.out.println("커넥트까지 오나요");
            accessToken = accessor.getFirstNativeHeader("Authorization");
            refreshToken = accessor.getFirstNativeHeader("Refresh");

            System.out.println("accessToken : " + accessToken);
            System.out.println("refreshToken : " + refreshToken);

            String jwtAccessToken = jwtUtil.socketResolveToken(accessToken);

            /* Access Token 검증 성공인 경우 */
            if (jwtAccessToken != null && jwtUtil.validateToken(jwtAccessToken) == JwtUtil.JwtCode.ACCESS) {
                log.info("엑세스 토큰 인증 성공");
            }

            /* Access 토큰이 만료된 경우 */
            else if (true) { // jwtAccessToken != null && jwtUtil.validateToken(jwtAccessToken) == JwtUtil.JwtCode.EXPIRED
                log.info("JWT 토큰이 만료되어, Refresh token 확인 작업을 진행합니다.");

                /* Refresh Token 존재 여부 확인.*/
                String jwtRefreshToken = jwtUtil.socketResolveToken(refreshToken);

                /* Refresh Token이 검증이 되며, 만료기간이 지나지 않은 경우만 */
                if (jwtRefreshToken != null && jwtUtil.validateRefreshToken(jwtRefreshToken) == JwtUtil.JwtCode.ACCESS) {
                    log.info("리프레시 토큰 인증 성공");
                }else {
                    throw new CustomErrorException(HttpStatus.OK, 200, "토큰이 일치하지 않습니다.");
                }
            } else {
                throw new CustomErrorException(HttpStatus.OK, 200, "토큰이 부정확합니다.");
            }
        }

        return message;
    }
}
