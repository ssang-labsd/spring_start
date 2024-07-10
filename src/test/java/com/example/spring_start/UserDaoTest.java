package com.example.spring_start;

import com.example.spring_start.user.dao.UserDao;
import com.example.spring_start.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {
    @Test
    public void addAndGet() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        UserDao dao = (UserDao) context.getBean("userDao",UserDao.class);
        User user1 = new User("gyumee","박성철","spring1");



        dao.deleteAll();
        assert(dao.getCount()==0);

        dao.add(user1);
        assert(dao.getCount()==1);

        User user2 = dao.get(user1.getId());

        assert(user2.getId().equals(user1.getId()));
        assert(user2.getName().equals(user1.getName()));
    }

    @Test
    public void count() throws SQLException {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao",UserDao.class);
        User user1 = new User("gyumee","박성철","spring1");
        User user2 = new User("leegw700","이길원","springno2");
        User user3 = new User("bumjin","박범진","springno3");

        dao.deleteAll();
        assert(dao.getCount()==0);

        dao.add(user1);
        assert(dao.getCount()==1);

        dao.add(user2);
        assert(dao.getCount()==2);

        dao.add(user3);
        assert(dao.getCount()==3);

    }
}
