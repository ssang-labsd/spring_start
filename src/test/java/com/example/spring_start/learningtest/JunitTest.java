package com.example.spring_start.learningtest;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;

public class JunitTest {
    static JunitTest testObject;

    @Test public void test1(){
        assert(this != testObject);
        testObject = this;
    }

    @Test public void test2(){
        assert(this != testObject);
        testObject = this;
    }

    @Test public void test3(){
        assert(this != testObject);
        testObject = this;
    }
}
