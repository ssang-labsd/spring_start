package com.example.spring_start;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(locations="/test-applicationContext.xml")
class SpringStartApplicationTests {

    @Test
    void contextLoads() {
    }

}
