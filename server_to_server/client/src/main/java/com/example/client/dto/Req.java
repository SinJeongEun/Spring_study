package com.example.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Req<T> {

    private Header header;
    private T rBody; //body는 계속해서 바뀌기 때문에 제너릭 타입으로 받는다.


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Header{
        private String responseCode;
    }
}
