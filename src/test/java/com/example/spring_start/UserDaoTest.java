package com.example.spring_start;

import com.example.spring_start.user.dao.UserDao;
import com.example.spring_start.user.domain.User;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

public class UserDaoTest {
    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        this.dao = context.getBean("userDao", UserDao.class);

        this.user1 = new User( "gyumee","박성철","spring1");
        this.user2 = new User("leegw700","이길원","springno2");
        this.user3 = new User("bumjin","박범진","springno3");
    }

    @Test
    public void addAndGet() throws Exception {


        dao.deleteAll();
        assert(dao.getCount()==0);

        dao.add(user1);
        dao.add(user2);
        assert(dao.getCount()==2);

        User userget1 = dao.get(user1.getId());
        assert(userget1.getName().equals(user1.getName()));
        assert(userget1.getPassword().equals(user1.getPassword()));

        User userget2 = dao.get(user2.getId());
        assert(userget2.getName().equals(user2.getName()));
        assert(userget2.getPassword().equals(user2.getPassword()));
    }

    @Test
    public void count() throws SQLException {


        dao.deleteAll();
        assert(dao.getCount()==0);

        dao.add(user1);
        assert(dao.getCount()==1);

        dao.add(user2);
        assert(dao.getCount()==2);

        dao.add(user3);
        assert(dao.getCount()==3);

    }

    @Test
    public void getUserFailure() throws SQLException{

        dao.deleteAll();
        // JUnit 5에서 바뀐 예외 기대 방식
        // 아래와 같이 발생을 기대하는 예외 클래스를 작성
        // 안에는 예외가 발생해야하는 메소드를 넣어주면 됨
        Assertions.assertThrows(EmptyResultDataAccessException.class,() ->{
            dao.get("unknown_id");
        });
    }

}
