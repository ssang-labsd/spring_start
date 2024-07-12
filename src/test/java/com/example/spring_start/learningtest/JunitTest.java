package com.example.spring_start.learningtest;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;

public class JunitTest {
    static Set<JunitTest> testObjects = new HashSet<JunitTest>();

    @Test public void test1(){
        assert(!testObjects.contains(this));
        testObjects.add(this);
    }

    @Test public void test2(){
        assert(!testObjects.contains(this));
        testObjects.add(this);
    }

    @Test public void test3(){
        assert(!testObjects.contains(this));
        testObjects.add(this);
    }
}
