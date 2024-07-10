package com.example.spring_start;

import com.example.spring_start.user.dao.DaoFactory;
import com.example.spring_start.user.dao.UserDao;
import com.example.spring_start.user.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        SpringApplication.run(SpringStartApplication.class, args);


        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao",UserDao.class);


        User user = new User();
        user.setId("yellowship");
        user.setName("여포");
        user.setPassword("complete");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }
}
