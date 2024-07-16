package com.example.spring_start.user.dao;

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
            this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)",
                    user.getId(), user.getName(), user.getPassword());
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
                 return user;
                }
            };

}
