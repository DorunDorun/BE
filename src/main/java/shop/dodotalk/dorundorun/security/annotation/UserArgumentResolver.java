package shop.dodotalk.dorundorun.security.annotation;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shop.dodotalk.dorundorun.security.jwt.OAuth2UserInfoAuthentication;

import java.util.Objects;

@Slf4j
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authenticated.class);

    }

    /**
     * SecurityContextHolder에서 받아온 authentication을 기준으로 AuthUserDetail을 찾아 반환한다.
     * @param parameter @Authenticated 어노테이션에 의해 받아온 파라미터.
     * @return Integer / AuthUserDetail - 파라미터의 타입에 따라 다른 형태로 반환.
     */

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication)) return null;


        if (isOAuth2UserInfoAuthenticationType(parameter))
            return authentication;
        else
            throw new IllegalArgumentException("지원하지 않는 타입입니다");
    }




    private boolean isOAuth2UserInfoAuthenticationType(MethodParameter parameter) {

        return OAuth2UserInfoAuthentication.class
                .isAssignableFrom(parameter.getParameterType());
    }
    private boolean isOAuth2UserInfoAuthenticationType(Authentication authentication) {

        return OAuth2UserInfoAuthentication.class
                .isAssignableFrom(authentication.getClass());
    }



}
