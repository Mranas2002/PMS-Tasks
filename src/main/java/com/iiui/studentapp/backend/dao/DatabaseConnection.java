package com.iiui.studentapp.backend.dao;

import com.iiui.studentapp.backend.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.URL,
                DatabaseConfig.USERNAME,
                DatabaseConfig.PASSWORD
        );
    }
}
