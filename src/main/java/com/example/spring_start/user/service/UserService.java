package com.example.spring_start.user.service;

import com.example.spring_start.user.dao.UserDao;
import com.example.spring_start.user.domain.Level;
import com.example.spring_start.user.domain.User;


import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Properties;



public class UserService {
    UserDao userDao;
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() throws Exception{
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try{
            List<User> users = userDao.getAll();
            for(User user : users){
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            this.transactionManager.commit(status);
        } catch(Exception e){
            this.transactionManager.rollback(status);
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
        sendUpgradeEMail(user);
    }

    public void add(User user){
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

    private void sendUpgradeEMail(User user){
        System.out.println(user.getName() + "님, 메일 보내기 완료!!");
    }
}
