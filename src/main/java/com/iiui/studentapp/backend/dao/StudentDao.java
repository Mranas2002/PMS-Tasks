package com.iiui.studentapp.backend.dao;

import com.iiui.studentapp.backend.model.StudentInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentDao {
    private static final String INSERT_SQL =
            "INSERT INTO students (email, reg_no, full_reg_no, name, degree_title, batch) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

    private final DatabaseConnection databaseConnection;

    public StudentDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void save(StudentInfo studentInfo) throws SQLException {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setString(1, studentInfo.getEmail());
            statement.setString(2, studentInfo.getRegNo());
            statement.setString(3, studentInfo.getFormattedRegNo());
            statement.setString(4, studentInfo.getName());
            statement.setString(5, studentInfo.getDegreeTitle());
            statement.setString(6, studentInfo.getBatch());
            statement.executeUpdate();
        }
    }
}
