package com.example.spring_start;

import com.example.spring_start.user.dao.UserDao;
import com.example.spring_start.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserDaoTest {
    @Test
    public void addAndGet() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        UserDao dao = (UserDao) context.getBean("userDao",UserDao.class);
        User user = new User();
        user.setId("gyumee");
        user.setName("박성철");
        user.setPassword("springno1");

        dao.add(user);

        User user2 = dao.get(user.getId());

        assert(user2.getId().equals(user.getId()));
        assert(user2.getName().equals(user.getName()));
    }
}
