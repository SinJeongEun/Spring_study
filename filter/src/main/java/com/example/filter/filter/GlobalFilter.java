package com.example.filter.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
//@Component // 스프링에서 빈으로 관리가 되도록
@WebFilter(urlPatterns = "/api/user/*") //특정 url에만 적용하려면 @Component  제거하고 이 어노테이션으로 명시한다.
public class GlobalFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //전처리
        //이때는 바디 정보를 미리 담아두는게 아니라 그 크기만 초기화한다. 담는건 후에,, dofilter()메소드가 실행되고 내부 스프링 안으로 들어가야 해당 바디를 read하여 담는다.
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);


        chain.doFilter(httpServletRequest,httpServletResponse); //이 함수를 기점으로 전처리, 후처리로 나뉜다.

        //후처리
        String url = httpServletRequest.getRequestURI();


        String reqContent = new String(httpServletRequest.getContentAsByteArray());
        log.info("request url : {}, requestBody : {}",url, reqContent);

        String resContent = new String(httpServletResponse.getContentAsByteArray()); // 컨트롤러를 다 타고 리스폰스를 담겨서 온다.
        //여기서 한번 더 읽었으므로 커서가 끝으로 가있으므로 .내용이 더 이상 없는 상황
        //따라서 내가 읽은 만큼 한번 더 복사가 필요하다.
        httpServletResponse.copyBodyToResponse();

        int httpStatus = httpServletResponse.getStatus();
        log.info("response status : {}, responseBody : {}",httpStatus, resContent );


//        BufferedReader br = httpServletRequest.getReader();
//        br.lines().forEach(line -> {
//            log.info("url : {}, line : {}",url,line);
//        });

    }
}
