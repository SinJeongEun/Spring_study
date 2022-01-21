이전과 다르게 Encoder 클래스의 bean 등록을 하지 않음 (@Component 제거함)
[**메인파일에서 빈을 직접 등록 함**](https://github.com/SinJeongEun/Spring_study/blob/master/ioc2/IocApplication.java)

```
@Configuration
class AppConfig{

    @Bean("base64Encode")
    public Encoder encoder(Base64Encoder base64Encoder){
        return new Encoder(base64Encoder);
    }

    @Bean("urlEncode")
    public Encoder encoder(UrlEncoder urlEncoder){
        return new Encoder(urlEncoder);
    }
}
```


- @Configuration : 하나의 클래스에서 여러 빈을 등록한다는 의미
- @Bean : 코드에서 new가 아닌 @Bean 로 미리 bean 등록

스프링에서 관리하는 객체 == bean
bean이 관리되는 장소 == 스프링 컨테이너
스프링 컨테이너가 객체 제어의 관리를 가져갔으므로 == 제어의 역전 == ioc

