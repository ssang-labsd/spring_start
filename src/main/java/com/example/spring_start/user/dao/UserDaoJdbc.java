package com.example.spring_start.user.dao;

import com.example.spring_start.user.domain.Level;
import com.example.spring_start.user.domain.User;
import com.example.spring_start.user.exception.DuplicateUserIdException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void add(final User user) throws DuplicateUserIdException {
        try{
            this.jdbcTemplate.update("insert into users(id, name, password,level,login,recommend) values(?,?,?,?,?,?)",
                    user.getId(), user.getName(), user.getPassword(),user.getLevel().intValue(),user.getLogin(),user.getRecommend());
        } catch(DuplicateUserIdException e) {
            throw new DuplicateUserIdException(e);
        }

    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",new Object[] {id}, this.userMapper);
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users",Integer.class);
    }

    public void update(User user1) {
        this.jdbcTemplate.update(
                "update users set name = ?, password = ?, level = ?, login = ?, " +
                    "recommend = ? where id = ?" , user1.getName(), user1.getPassword(), user1.getLevel().intValue(), user1.getLogin(), user1.getRecommend(), user1.getId()
        );
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", this.userMapper);
    }

    private RowMapper<User> userMapper =
            new RowMapper<User>() {
             public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                 User user = new User();
                 user.setId(rs.getString("id"));
                 user.setName(rs.getString("name"));
                 user.setPassword(rs.getString("password"));
                 user.setLevel(Level.valueOf(rs.getInt("level")));
                 user.setLogin(rs.getInt("login"));
                 user.setRecommend(rs.getInt("recommend"));
                 return user;
                }
            };

}
