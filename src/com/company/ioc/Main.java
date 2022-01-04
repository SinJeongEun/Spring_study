package com.company.ioc;

public class Main {

    public static void main(String[] args) {
	    String url = "www.naver.com/books/it?page=10&size=20&name=spring-boot";
            //DI 적용 전
//Base64 encoding
//        IEncoder base64Encoder = new Base64Encoder();
//        String result1 = base64Encoder.encode((url));
//
// url encoding
//        IEncoder urlEncoder = new UrlEncoder();
//        String urlResult = urlEncoder.encode(url);

        //        DI 적용 후
        Encoder encoder = new Encoder(new UrlEncoder());  //내부 코드 수정 필요 없이 주입되는 객체에 따라 동작되며 코드 관리와 재사용성이 향상 됨
        String result = encoder.encode(url);


        System.out.println(result);
    }
}
