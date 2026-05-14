package com.example.securecapita.repo.implementation;

import com.example.securecapita.exception.ApiException;
import com.example.securecapita.model.Role;
import com.example.securecapita.repo.RoleRepository;
import com.example.securecapita.rowmapper.RoleRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.example.securecapita.enumeration.RoleType.ROLE_USER;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImplementation implements RoleRepository<Role> {

    private static final String SELECT_ROLE_BY_NAME_QUERY = "";
    private static final String INSERT_ROLE_TO_USER_QUERY =  "";
    private final NamedParameterJdbcTemplate jdbc = null;

    @Override
    public Role create(Role data) {
        return null;
    }

    @Override
    public Collection<Role> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public Role get(Long id) {
        return null;
    }

    @Override
    public Role update(Role data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {
        log.info("Adding role to {} user id: { }",roleName,userId);
        try{
            Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY,Map.of("roleName",roleName),new RoleRowMapper());
            jdbc.update(INSERT_ROLE_TO_USER_QUERY,Map.of("userId",userId,"roleId",role.getId()));
        }
        catch(Exception exception){
            throw new ApiException(" An error occurred try again");
        }
    }

    @Override
    public Role getRoleByUserId(Long userId) {
        return null;
    }

    @Override
    public Role getRoleByUserEmail(String email) {
        return null;
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {

    }
}
