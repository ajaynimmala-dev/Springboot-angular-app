package com.example.securecapita.rowmapper;

import com.example.securecapita.model.Role;
import com.example.securecapita.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .address(resultSet.getString("address"))
                .phone(resultSet.getString("phone"))
                .title(resultSet.getString("title"))
                .bio(resultSet.getString("bio"))
                .imageUrl(resultSet.getString("image_url"))
                .enabled(resultSet.getBoolean("enabled"))
                .isUsingMfa(resultSet.getBoolean("is_using_mfa"))
                .isNotLocked(resultSet.getBoolean("is_not_locked"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .build();

    }
}
