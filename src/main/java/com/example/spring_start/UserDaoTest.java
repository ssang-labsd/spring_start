package com.example.spring_start;

import com.example.spring_start.user.dao.UserDaoJdbc;
import com.example.spring_start.user.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        SpringApplication.run(SpringStartApplication.class, args);


        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDaoJdbc dao = context.getBean("userDao", UserDaoJdbc.class);


        User user = new User();
        user.setId("redship");
        user.setName("토비");
        user.setPassword("java");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }
}
