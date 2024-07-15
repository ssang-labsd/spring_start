package com.example.spring_start.user.dao;

import com.example.spring_start.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {
    private DataSource dataSource;
    private Connection c;
    private JdbcContext jdbcContext;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        this.dataSource = dataSource;
    }


    public void add(final User user) throws SQLException {
        this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws SQLException {
        this.c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        User user = null;

        if(rs.next()){
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if (user==null) throw new EmptyResultDataAccessException(1);


        return user;
    }

    public void deleteAll() throws SQLException {
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() throws SQLException {
        return this.jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement("select count(*) from users");
            }
        },new ResultSetExtractor<Integer>(){
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return rs.getInt(1);
            }
        });
    }

}
