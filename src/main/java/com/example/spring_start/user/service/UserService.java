package com.example.spring_start.user.service;

import com.example.spring_start.user.dao.UserDao;
import com.example.spring_start.user.domain.Level;
import com.example.spring_start.user.domain.User;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

public class UserService {
    UserDao userDao;
    private DataSource dataSource;
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void upgradeLevels() throws Exception{
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);

        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try{
            List<User> users = userDao.getAll();
            for(User user : users){
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            transactionManager.commit(status);
        } catch(Exception e){
            transactionManager.rollback(status);
            throw e;
        }
    }

    private boolean canUpgradeLevel(User user){
        Level currentLevel = user.getLevel();
        switch(currentLevel){
            case BASIC: return (user.getLogin() >=MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >=MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unkown Level: "+currentLevel);
        }
    }

    protected void upgradeLevel(User user){
        user.upgradeLevel();
        userDao.update(user);
    }

    public void add(User user){
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
