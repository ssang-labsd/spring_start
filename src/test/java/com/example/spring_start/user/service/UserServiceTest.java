package com.example.spring_start.user.service;

import com.example.spring_start.user.dao.UserDao;
import com.example.spring_start.user.dao.UserDaoJdbc;
import com.example.spring_start.user.domain.Level;
import com.example.spring_start.user.domain.User;
import com.example.spring_start.mail.MailSender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static com.example.spring_start.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.example.spring_start.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {
    @Autowired
    UserService userService;
    List<User> users; // 테스트 픽스처
    @Autowired
    private UserDao userDao;
    @Autowired
    DataSource dataSource;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    MailSender mailSender;
    @Autowired
    UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User("bumjin","박범진","p1", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER-1,0),
                new User("joytouch","강명성","p2",Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0),
                new User("erwins", "신승한", "p3",Level.SILVER,60,MIN_RECOMMEND_FOR_GOLD-1),
                new User("madnite1", "이상호", "p4",Level.SILVER,60,MIN_RECOMMEND_FOR_GOLD),
                new User("green", "오민규", "p5",Level.GOLD,100,Integer.MAX_VALUE)
        );
    }

    @Test
    public void upgradeLevels() throws Exception{
        userDao.deleteAll();
        for(User user : users){
            userDao.add(user);
        }

        userService.upgradeLevels();
        checkLevelUpgraded(users.get(0),false);
        checkLevelUpgraded(users.get(1),true);
        checkLevelUpgraded(users.get(2),false);
        checkLevelUpgraded(users.get(3),true);
        checkLevelUpgraded(users.get(4),false);
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4); // GOLD 레벨
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assert(userWithLevelRead.getLevel().equals(userWithLevel.getLevel()));
        assert(userWithoutLevelRead.getLevel().equals(Level.BASIC));
    }

    @Test
    public void upgradeAllOrNothing() throws Exception{
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);


        UserServiceTx txUserService = new UserServiceTx();
        txUserService.setTransactionManager(transactionManager);
        txUserService.setUserService(testUserService);



        userDao.deleteAll();

        for(User user: users) userDao.add(user);
        try{
            txUserService.upgradeLevels();
            fail("TestUserService Exception expected");
        }
        catch(TestUserServiceException e){

        }
        checkLevelUpgraded(users.get(1),false);
        userServiceImpl.setMailSender(mailSender);
    }

    private void checkLevelUpgraded(User user, boolean upgraded){
        User userUpdate = userDao.get(user.getId());
        if (upgraded){
            assert(userUpdate.getLevel().equals(user.getLevel().nextLevel()));
        }
        else{
            assert(userUpdate.getLevel().equals(user.getLevel()));
        }

    }

    static class TestUserService extends UserServiceImpl{
        private String id;

        private TestUserService(String id){
            this.id = id;
        }

        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }

    }

    static class TestUserServiceException extends RuntimeException{

    }
}


