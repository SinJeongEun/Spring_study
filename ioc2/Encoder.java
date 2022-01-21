package com.example.ioc2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


public class Encoder {

    private IEncoder iEncoder;

    // IEncoder 컴포넌트가 2개(base64Encoder,urlEncoder) 존재하므로 어느것을 매칭하는지 모름
    //따라서 @Qualifier("")사용하여 명시한다.
    public Encoder(IEncoder iEncoder){
        this.iEncoder = iEncoder;
    }

    public void setiEncoder(IEncoder iEncoder){
        this.iEncoder = iEncoder;
    }

    public String encode(String message){
        return iEncoder.encode(message);
    }
}
