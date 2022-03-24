#### AOP Aspect Oriented Programming
관점지향 프로그램

공통 메소드나 특정구역에 반복되는 로직들을 한 곳으로 몰아서 코딩 가능하게 한다.
일관된 비지니스 로직 구현 가능
custom_annotation을 사용하여 특정메소드,특정 기관에 대한 요청을 수용 가능(encode 등)
controller 전후에 적용시 디버깅 용이

- @Aspect  : AOP를 정의하는 Class에 할당
- @Pointcut :  기능을 어디에 적용시킬지(메소드,어노테이션 등)AOP를 적용 시킬 지점을 설정
- @Before  : 메소드 실행하기 이전
- @After: 메소드가 성공적으로 실행 후 예외가 발생되더라도 실행
- @AfterReturning :  메소드 호출 성공 실행 시 (not Throw)
- @AfterThrowing :  메소드 호출 실패 예외 발생(Throws)
- @Around : Before/After 모두 제어 ,예외 발생해도 실행


- @JoinPoint : 들어가는 지점에 대한 정보를 가진 객체


<br>
### custom annotation 
<br>
// filter , interceptor에서의 변환은 tomcat 에서 body를 한 번 읽으면 더이상 읽을 수 없도록 막아둠 . => 변환이 어려움 <br>
=> aop 구간은 이미 filter, interceptor을 지나서 값 자체가 "객체화"됐으므로 값 변환이 가능 (=> 암호화된 데이터를 서비스 단에서 코드로 복호화가 아닌 aop단에서 복호화 가능)
=> 반복적으로 사용되는 로직을 어노테이션으로 대체함으로써 서비스 로직이 분명해짐
