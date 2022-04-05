package com.example.interceptor.interceptor;

import com.example.interceptor.annotation.Auth;
import com.example.interceptor.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //filter 단에서!!! ContentCachingRequestWrapper 로 형변환 하여 보내면
        //interceptor 에서도 역시 HttpServletRequest-> ContentCachingRequestWrapper 형 변환이 가능하다.
        String url = request.getRequestURI();

        URI uri = UriComponentsBuilder.fromUriString(url)
                .query(request.getQueryString())
                .build()
                .toUri();
        log.info("url : {}", url);

        //권한 체크
        boolean hasAnnotation = checkAnnotation(handler, Auth.class);
        log.info("has annotation : {}",hasAnnotation);

        //나의 서버느 모두 public으로 동작하는데
        // 단 Auth  권한을 가진 요청에 대해서는 세션, 쿠키 검사함
        if(hasAnnotation){
            //권한 체크
            String query = uri.getQuery();

            log.info("query : {}",query);

            if(query.equals("name=steve")) return true;
            throw new AuthException("steve가 아님");
        }

        return true; // false 인 경 handlerInterceptor -> handler 로 들어가지 못함 (그림 참고)
    }

    private boolean checkAnnotation(Object handler, Class clazz) { //clazz 는 해당 auth annotation class 를 의미

        //resource  : javascript, html 은 바로 통과
        if(handler instanceof ResourceHttpRequestHandler) return true;

        //annotation check
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if(null != handlerMethod.getMethodAnnotation(clazz) || null != handlerMethod.getBeanType().getAnnotation(clazz)){
            return true;
        }

        return false;

    }
}
