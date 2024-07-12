package com.example.spring_start.learningtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class JunitTest {
    @Autowired
    ApplicationContext context;

    static Set<JunitTest> testObjects = new HashSet<JunitTest>();
    static ApplicationContext contextObject = null;

    @Test public void test1(){
        assert(!testObjects.contains(this));
        testObjects.add(this);

        assert(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

    @Test public void test2(){
        assert(!testObjects.contains(this));
        testObjects.add(this);

        assert(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

    @Test public void test3(){
        assert(!testObjects.contains(this));
        testObjects.add(this);

        assert(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }
}
