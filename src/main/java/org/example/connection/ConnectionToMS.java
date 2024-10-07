package org.example.connection;

import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionToMS {

    private String url;
    private String password;
    private String username;
    private static final int POOL_SIZE = 10;
    private static BlockingQueue<Connection> pool;

    static {
        try {
            initConnectionPool();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initConnectionPool() throws ClassNotFoundException {
        pool = new ArrayBlockingQueue(POOL_SIZE);
        for(int i = 0; i < POOL_SIZE; i++){

            Connection connection = open();
            var proxyConnection = (Connection)Proxy.newProxyInstance(ConnectionToMS.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args)-> method.getName().equals("close") ? pool.add((Connection) proxy) : method.invoke(connection, args));
            pool.add(proxyConnection);
        }
    }

    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection open() throws ClassNotFoundException {
        String connectionUrl =
                "jdbc:sqlserver://localhost:1433;DatabaseName=детский_летний_лагерь;user=userok;password=13ab7654;encrypt=true;trustServerCertificate=true";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        try{
            return DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
