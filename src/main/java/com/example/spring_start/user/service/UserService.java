package com.example.spring_start.user.service;

import com.example.spring_start.user.dao.UserDao;
import com.example.spring_start.user.dao.UserDaoJdbc;
import com.example.spring_start.user.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();

    void setUserDao(UserDao userDao);
}
