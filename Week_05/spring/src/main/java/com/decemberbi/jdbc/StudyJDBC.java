package com.decemberbi.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudyJDBC {

    private static final String URL = "jdbc:mysql://localhost:3306/study_jdbc?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";

    private static final String USERNAME = "biykcode";

    private static final String PASSWORD = "123456";

    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    public static DataSource hikariCP() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(DRIVER_NAME);
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USERNAME);
        hikariDataSource.setPassword(PASSWORD);
        hikariDataSource.setReadOnly(false);
        hikariDataSource.setConnectionTimeout(30000);
        hikariDataSource.setIdleTimeout(60000);
        hikariDataSource.setMaxLifetime(1800000);
        hikariDataSource.setMaximumPoolSize(10);
        return hikariDataSource;
    }

    public static void main(String[] args) throws Exception {
        // 原生方式
        origin();
        // 事务和PrepareStatement
        txAndPrepareStatement();
        // 使用HiKariCP获取连接
        hikariCPConnect();
    }

    private static void hikariCPConnect() throws SQLException {
        DataSource dataSource = hikariCP();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preStatement(connection, preparedStatement);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (connection != null)
                connection.close();
        }
    }

    private static void preStatement(Connection connection, PreparedStatement preparedStatement) throws SQLException {
        connection.setAutoCommit(false);
        // 增
        String insertSql = "insert into user(id , name) values(?, ?)";
        preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, 5);
        preparedStatement.setString(2, "bi2222");
        int insertCount = preparedStatement.executeUpdate();
        System.out.println("insert count = " + insertCount);
        // 手动抛异常，看事务的回滚
//        int a = 5 / 0;
        // 改
        String updateSql = "update user set name = ? where id = ?";
        preparedStatement = connection.prepareStatement(updateSql);
        preparedStatement.setString(1, "bi3333");
        preparedStatement.setInt(2, 5);
        int updateCount = preparedStatement.executeUpdate();
        System.out.println("update count = " + updateCount);
        // 查
        String querySql = "select * from user where id = ?";
        preparedStatement = connection.prepareStatement(querySql);
        preparedStatement.setInt(1, 5);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            User user = new User(id, name);
            System.out.println(user);
        }

        // 删
        String deletSql = "delete from user where id = ?";
        preparedStatement = connection.prepareStatement(deletSql);
        preparedStatement.setInt(1, 5);
        int deleteCount = preparedStatement.executeUpdate();
        System.out.println("delete count = " + deleteCount);
    }

    /**
     * 使用事务和Prestatement
     *
     * @throws SQLException
     */
    private static void txAndPrepareStatement() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (connection != null)
                connection.close();
        }
    }

    /**
     * 原生方式处理
     */
    private static void origin() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            // 增
            int insertCount = statement.executeUpdate("insert into user(id , name) values(1, 'bi')");
            statement.executeUpdate("insert into user(id , name) values(2, 'bii')");
            System.out.println("insert count = " + insertCount);
            // 改
            int updateCount = statement.executeUpdate("update user set name = 'december' where id = 1");
            System.out.println("update count = " + updateCount);
            // 查
            ResultSet resultSet = statement.executeQuery("select * from user");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                User user = new User(id, name);
                System.out.println(user);
            }
            // 删
            int deleteCount = statement.executeUpdate("delete from user where id = 2");
            System.out.println("delete count = " + deleteCount);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
    }
}
