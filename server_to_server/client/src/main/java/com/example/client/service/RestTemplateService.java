package com.example.client.service;

import com.example.client.dto.Req;
import com.example.client.dto.UserRequest;
import com.example.client.dto.UserResponse;
import org.apache.catalina.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Queue;

@Service
public class RestTemplateService {

    //http://localhsot/api/server/hello 를 통해
    //response 를 받아온다.
    public UserResponse hello(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("api/server/hello")
                .queryParam("name","steve")
                .queryParam("age",99)
                .encode() //만약 파라이터가 붙는다면 안정적인 인코딩을 위해서 추가한다.
                .build()
                .toUri();

        System.out.println(uri);
        //restTemplate 를 통해 통신한다.
        RestTemplate restTemplate = new RestTemplate();

        //아래 함수가 실행되는 순간이 client가  http로 serverd에 붙는 순간이다.
        //0bject 형태로 get(http method)
//        String result = restTemplate.getForObject(uri, String.class); //뒤에 지정한 타입(string)으로 응답을 받을 수 있다.

        //ResponseExtity 인터페이스 이므로  status code, body 등 상세 정보를 불러 올 수 있다.
        // <> 안에는 응답 받고자 하는 타입을 명시하면 된다.
        ResponseEntity<UserResponse> result = restTemplate.getForEntity(uri,UserResponse.class);
        System.out.println(result.getStatusCode());
        System.out.println(result.getBody());
        return  result.getBody();
    }

    //http://localhsot/api/server/user/{userId}/name/{username}
    public UserResponse post(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{username}")
                .encode()
                .build()
                .expand(100,"lisa")
                .toUri();
        System.out.println(uri);

        //http body -> object -> object mapper -> json -> rest template -> http body json
        UserRequest req = new UserRequest();
        req.setAge(24);
        req.setName("lisa");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResponse> response = restTemplate.postForEntity(uri,req,UserResponse.class);

        System.out.println(response.getBody());
        return response.getBody();
    }

    //위와 같은 상황에서 header 세팅이 필요한 경우
    public UserResponse exchange(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{username}")
                .encode()
                .build()
                .expand(120,"lisa")
                .toUri();
        System.out.println(uri);

        //http body -> object -> object mapper -> json -> rest template -> http body json
        UserRequest req = new UserRequest();
        req.setAge(24);
        req.setName("lisa");

        RequestEntity<UserRequest> requestEntity =  RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-authorization","abcd")
                .header("custom-header","fffff")
                .body(req);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResponse> response = restTemplate.exchange(requestEntity,UserResponse.class);
        return response.getBody();

    }

    //body의 정보가 고정되지 않을 때
    // 요청 클래스를 제네릭 타입으로 만들고 이 객체를 전달하면 된다.
    public Req<UserResponse> genericExchange(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{username}")
                .encode()
                .build()
                .expand(120,"lisa")
                .toUri();
        System.out.println(uri);

        //제네릭 타입 요청 안에 넣을 body 내용
        UserRequest userRequest = new UserRequest();
        userRequest.setAge(24);
        userRequest.setName("lisa");

        //제네릭 타입의 요청을 생성한다.
        Req<UserRequest> req = new Req<>();
        req.setHeader(new Req.Header());

        req.setRBody(userRequest);

        RequestEntity<Req<UserRequest>> requestEntity =  RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-authorization","abcd")
                .header("custom-header","fffff")
                .body(req);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Req<UserResponse>> response
                = restTemplate.exchange(requestEntity,
                new ParameterizedTypeReference<Req<UserResponse>>() {
                });  //제네릭 타입은 .class로 안됨으로  ParameterizedTypeReference를 통해 주입한다.

        return  response.getBody();
    }

}
