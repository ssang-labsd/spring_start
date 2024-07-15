package com.example.spring_start.learningtest.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CalcSumTest {
    Calculator calculator;
    String numFilepath;

    @BeforeEach
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilepath = getClass().getResource("/numbers.txt").getPath();
    }
    @Test
    public void sumOfNumbers() throws IOException {
        assert(calculator.calcSum(this.numFilepath) == 10);
    }

    @Test
    public void multiplyOfNumbers() throws IOException {
        assert(calculator.calcMultiply(this.numFilepath) == 24);
    }
}
