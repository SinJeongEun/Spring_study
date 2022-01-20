package com.example.ioc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class IocApplication {

    public static void main(String[] args) {
        SpringApplication.run(IocApplication.class, args);

        //스프링 어플리케이션이 실행되고 나면 가져오도록
        ApplicationContext context = ApplicationContextProvider.getContext();

        //getBean을 통한 객체 가져오기 (new를 통한 객체생성x)
//        Base64Encoder base64Encoder = context.getBean(Base64Encoder.class);
//        UrlEncoder urlEncoder = context.getBean(UrlEncoder.class);

//        Encoder encoder = new Encoder(base64Encoder);
        Encoder encoder = context.getBean(Encoder.class);
        String url = "www.naver.com/books/it?page=10&size=20&name=spring-boot";

        String result = encoder.encode(url);
        System.out.println(result);

//        encoder.setiEncoder(urlEncoder);
//        result = encoder.encode(url);
//        System.out.println(result);
    }


}
