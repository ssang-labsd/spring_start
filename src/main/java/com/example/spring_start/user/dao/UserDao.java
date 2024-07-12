package com.example.spring_start.user.dao;

import com.example.spring_start.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {
    private ConnectionMaker connectionMaker;
    private DataSource dataSource;
    private Connection c;
    private User user;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void add(User user) throws SQLException {

        this.c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
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
        Connection c = null;
        PreparedStatement ps = null;
    try{
        c = dataSource.getConnection();
        ps = c.prepareStatement("delete from users");
        ps.executeUpdate();

    } catch(SQLException e){
        throw e;
    } finally {
        if (ps !=null){
            try {
                ps.close();
            } catch (SQLException e) {

            }
        }
        if (c !=null){
            try {
                c.close();
            } catch (SQLException e) {

            }
        }
    }
    }

    public int getCount() throws SQLException {
        this.c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select count(*) from users");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        c.close();

        return count;

    }
}
