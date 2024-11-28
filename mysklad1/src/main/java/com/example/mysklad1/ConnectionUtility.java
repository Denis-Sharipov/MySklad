package com.example.mysklad1;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConnectionUtility {
    private Connection connection;

    public Connection getConnection() throws SQLException{
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?useSSL=false&requireSSL=false&allowPublicKeyRetrieval=true&user=root&password=228");
        return connection;
    }

    public void close() throws SQLException { //Закрытие подключения к БД.
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}
