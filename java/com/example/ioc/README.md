ioc는 스프링 컨테이너에서 관리하므로
프로젝트 자체를 스프링으로 만들어야 됨

- ioc
  클래스 위에 @Component -> bean으로 등록된다.  @Component("cName") 으로 bean의 name 등록 가능
  스프링이 실행될 때 어노테이션이 붙dms class를 찾아서  직접 객체를 싱글톤 형태로 만들어서 스프링 컨테이너에서 관리함
  ==>제어의 역전
  어플리케이션이 관리하므로 ioc라고 함

- 스프링컨테이너에 접근하여 객체를 가져오는 방법
  ```
  Base64Encoder base64Encoder = context.getBean(Base64Encoder.class);
  UrlEncoder urlEncoder = context.getBean(UrlEncoder.class);
  Encoder encoder = context.getBean(Encoder.class);
  ```

- (**한가지 Component만 가능한 Encoder 코드**)(https://github.com/SinJeongEun/Spring_study/blob/master/java/com/example/ioc/Encoder.java)


- 빈을 주입받을 수 있는 곳3
  변수,생성자,set메소드
