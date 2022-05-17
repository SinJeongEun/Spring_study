package com.example.testcode.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Calculator {

    private final ICalculator iCalculator;

    public int sum(int x , int y){
        this.iCalculator.init();
        return iCalculator.sum(x,y);
    }

    public int minus(int x, int y){
        this.iCalculator.init();
        return iCalculator.minus(x,y);
    }
}
