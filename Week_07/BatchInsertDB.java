package com.decemberbi.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.checkerframework.checker.units.qual.A;

import javax.sql.DataSource;
import java.sql.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchInsertDB {

    private static final String URL = "jdbc:mysql://localhost:3306/test1?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true";

    private static final String USERNAME = "biykcode";

    private static final String PASSWORD = "123456";

    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    private static String sql = "insert into t_order(user_id, product_id, price, quantity, total_payment, pay_status, address, create_time) " +
            "values(?, ?, ?, ?, ?, ?, ?, now())";

    private static String originSql = "insert into t_order(user_id, product_id, price, quantity, total_payment, pay_status, address, create_time) values ";

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception {
//        txAndPrepareStatement();
//        originBatch();
//        singleInsertBatch();
//        multiThreadInsertBatch();
        multiThreadAndConnectionInsertBatch();
    }

    public static DataSource hikariCP() throws SQLException {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(DRIVER_NAME);
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USERNAME);
        hikariDataSource.setPassword(PASSWORD);
        hikariDataSource.setReadOnly(false);
        hikariDataSource.setConnectionTimeout(30000);
        hikariDataSource.setIdleTimeout(60000);
        hikariDataSource.setMaxLifetime(1800000);
        hikariDataSource.setValidationTimeout(3000);
        hikariDataSource.setLoginTimeout(5);
        hikariDataSource.setMaximumPoolSize(10);
        return hikariDataSource;
    }

    /**
     * FIXME 多线程下超过10个连接的处理没有思路
     *  超过10个任务提交到线程池，就会获取超过10个数据库连接，就会报错
     * @throws SQLException
     */
    private static void multiThreadAndConnectionInsertBatch() throws SQLException {
        long startTime = System.currentTimeMillis();
        System.out.println("开始插入时间：" + startTime);
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        DataSource dataSource = hikariCP();
        try {
            AtomicInteger insertCount = new AtomicInteger(0);
            for (int i = 0; i < 1000; i++) {
                executorService.execute(() -> {
                    Connection connection = null;
                    try {
                        connection = dataSource.getConnection();
                        connection.setAutoCommit(false);
                        Statement statement = connection.createStatement();
                        String sql = originSql;
                        for (int k = 0; k < 1000; k++) {
                            sql += "(100, 200, 100, 1, 100, 0, 'beijing', now()),";
                        }
                        statement.executeUpdate(sql.substring(0, sql.length() - 1));
                        insertCount.incrementAndGet();
                        countDownLatch.countDown();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
            }
            countDownLatch.await();
            System.out.println("insert count = " + insertCount.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("结束时间：" + endTime);
        System.out.println("花费时长：" + (endTime - startTime) / 1000);
        executorService.shutdown();
    }

    private static void multiThreadInsertBatch() throws SQLException {
        long startTime = System.currentTimeMillis();
        System.out.println("开始插入时间：" + startTime);
        Connection connection = null;
        Statement statement = null;
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        try {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            // 增
            AtomicInteger insertCount = new AtomicInteger(0);
            for (int i = 0; i < 1000; i++) {
                Statement finalStatement = statement;
                executorService.execute(() -> {
                    String sql = originSql;
                    for (int k = 0; k < 1000; k++) {
                        sql += "(100, 200, 100, 1, 100, 0, 'beijing', now()),";
                    }
                    try {
                        finalStatement.executeUpdate(sql.substring(0, sql.length() - 1));
                        insertCount.incrementAndGet();
                        countDownLatch.countDown();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
            }
            countDownLatch.await();
            System.out.println("insert count = " + insertCount.get());
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("结束时间：" + endTime);
        System.out.println("花费时长：" + (endTime - startTime) / 1000);
    }


    private static void singleInsertBatch() throws SQLException {
        long startTime = System.currentTimeMillis();
        System.out.println("开始插入时间：" + startTime);
        Connection connection = null;
        Statement statement = null;
        CountDownLatch countDownLatch = new CountDownLatch(1000000);
        try {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            // 增
            AtomicInteger insertCount = new AtomicInteger(0);
            for (int i = 0; i < 1000000; i++) {
                Statement finalStatement = statement;
                executorService.execute(() -> {
                    String sql = originSql + "(100, 200, 100, 1, 100, 0, 'beijing', now()),";
                    try {
                        finalStatement.executeUpdate(sql.substring(0, sql.length() - 1));
                        insertCount.incrementAndGet();
                        countDownLatch.countDown();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
            }
            countDownLatch.await();
            System.out.println("insert count = " + insertCount.get());
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("结束时间：" + endTime);
        System.out.println("花费时长：" + (endTime - startTime) / 1000);
    }

    private static void originBatch() throws SQLException {
        long startTime = System.currentTimeMillis();
        System.out.println("开始插入时间：" + startTime);
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            // 增
            int insertCount = 0;
            for (int k = 0; k < 1000; k++) {
                String sql = originSql;
                for (int i = 0; i < 1000; i++) {
                    sql = sql + "(100, 200, 100, 1, 100, 0, 'beijing', now()),";
                }
                insertCount = insertCount + statement.executeUpdate(sql.substring(0, sql.length() - 1));
                System.out.println("第" + k + "次插入总数：" + insertCount);
            }
            System.out.println("insert count = " + insertCount);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("结束时间：" + endTime);
        System.out.println("花费时长：" + (endTime - startTime) / 1000);
    }

    private static void txAndPrepareStatement() throws SQLException {
        long startTime = System.currentTimeMillis();
        System.out.println("开始插入时间：" + startTime);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preStatement(connection, preparedStatement);
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
        long endTime = System.currentTimeMillis();
        System.out.println("结束时间：" + endTime);
        System.out.println("花费时长：" + (endTime - startTime) / 1000);
    }

    private static void preStatement(Connection connection, PreparedStatement preparedStatement) throws SQLException {
        connection.setAutoCommit(false);
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < 1000000; i++) {
            preparedStatement.setInt(1, 100 + i);
            preparedStatement.setInt(2, 200 + i);
            preparedStatement.setInt(3, 100);
            preparedStatement.setInt(4, 1);
            preparedStatement.setInt(5, 100);
            preparedStatement.setInt(6, 0);
            preparedStatement.setString(7, "beijing");
            preparedStatement.addBatch();
        }
        int[] intArr = preparedStatement.executeBatch();
        System.out.println("insert count = " + intArr.length);

    }
}
