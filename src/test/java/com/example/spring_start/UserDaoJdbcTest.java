package com.example.spring_start;

import com.example.spring_start.user.dao.UserDao;
import com.example.spring_start.user.domain.Level;
import com.example.spring_start.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserDaoJdbcTest {
    @Autowired UserDao dao;
    @Autowired DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        this.user1 = new User( "gyumee","박성철","spring1", Level.BASIC,1,0);
        this.user2 = new User("leegw700","이길원","springno2",Level.SILVER,55,10);
        this.user3 = new User("bumjin","박범진","springno3",Level.GOLD,100,40);
    }

    @Test
    public void addAndGet() throws Exception {


        dao.deleteAll();
        assert(dao.getCount()==0);

        dao.add(user1);
        dao.add(user2);
        assert(dao.getCount()==2);

        User userget1 = dao.get(user1.getId());
        checkSameUser(userget1,user1);

        User userget2 = dao.get(user2.getId());
        checkSameUser(userget2,user2);
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
        assertThrows(EmptyResultDataAccessException.class,() ->{
            dao.get("unknown_id");
        });
    }

    @Test
    public void getAll() throws SQLException {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assert(users0.size() == 0);

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assert(users1.size()==1);
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assert(users2.size()==2);
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assert(users3.size()==3);
        // getAll은 오브젝트 내용을 ID 순으로 정렬해서 반환함
        // 아래와 같이 Id 정렬 순서를 고려해야함
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
    }

    @Test
    public void duplicateKey() {
        dao.deleteAll();

        dao.add(user1);
        assertThrows(DataAccessException.class,()->{
            dao.add(user1);
        });

    }

    @Test
    public void sqlExceptionTranslate(){
        dao.deleteAll();

        try{
            dao.add(user1);
            dao.add(user1);
        } catch(DuplicateKeyException ex){
            SQLException sqlEx = (SQLException)ex.getRootCause();
            //SQLException 전환
            SQLExceptionTranslator set =
                    new SQLErrorCodeSQLExceptionTranslator(this.dataSource);

            assert(set.translate(null,null,sqlEx).getClass().equals(DuplicateKeyException.class));
        }
    }

    @Test
    public void update(){
        dao.deleteAll();

        dao.add(user1);  // 수정할 사용자
        dao.add(user2);  // 수정하지 않을 사용자

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1,user1update);
        User user2same = dao.get(user2.getId());
        checkSameUser(user2, user2same);
    }

    private void checkSameUser(User user1, User user2) {
        assert(user1.getId().equals(user2.getId()));
        assert(user1.getName().equals(user2.getName()));
        assert(user1.getPassword().equals(user2.getPassword()));
        assert(user1.getLevel().equals(user2.getLevel()));
        assert(user1.getLogin() == user2.getLogin());
        assert(user1.getRecommend() == user2.getRecommend());
    }


}
