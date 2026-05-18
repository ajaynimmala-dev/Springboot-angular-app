package com.example.securecapita.query;

public class RoleQuery {
    public static final String INSERT_ROLE_TO_USER_QUERY = "INSERT INTO usersroles (user_id, role_id) VALUES (:userId, :roleId)";
    public static final String SELECT_ROLE_BY_NAME_QUERY = "SELECT * FROM role WHERE name = :name";
    public static final String SELECT_ROLE_BY_ID_QUERY =""+
            "SELECT  r.id,r.name,r.permission FROM role r JOIN usersroles ur ON r.id = ur.role_id JOIN users u on u.id= ur.user_id WHERE u.id = :id";
}
