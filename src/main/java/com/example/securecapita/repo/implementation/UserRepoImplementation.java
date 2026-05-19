package com.example.securecapita.repo.implementation;

import com.example.securecapita.dto.UserDTO;
import com.example.securecapita.exception.ApiException;
import com.example.securecapita.model.Role;
import com.example.securecapita.model.User;
import com.example.securecapita.model.UserPrincipal;
import com.example.securecapita.repo.RoleRepository;
import com.example.securecapita.repo.UserRepository;
import com.example.securecapita.rowmapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

import static com.example.securecapita.enumeration.RoleType.ROLE_USER;
import static com.example.securecapita.enumeration.VerificationType.ACCOUNT;
import static com.example.securecapita.query.UserQuery.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.time.DateFormatUtils.format;
import static org.apache.commons.lang3.time.DateUtils.addDays;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepoImplementation implements UserRepository<User>, UserDetailsService {

    private final NamedParameterJdbcTemplate jdbc;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;
    private final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    @Override
    public User create(User user) {
        //check email is unique
        if(getEmailCount(user.getEmail().trim().toLowerCase())>0){
            throw new ApiException("Email already exists use a different email and try again");
        }
        try{
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameters(user);
            //save new user
            jdbc.update(INSERT_USER_QUERY,parameters,holder);
            Map<String, Object> keys = holder.getKeys();
            user.setId(((Number) keys.get("id")).longValue());
            //Add role to user
            roleRepository.addRoleToUser(user.getId(),ROLE_USER.name());
            //send verification url
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(),ACCOUNT.getType());
            //save url in verification table
            //System.out.println(verificationUrl);
            jdbc.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY,Map.of("userId",user.getId(),"url",verificationUrl));
            //send email to user with verification url
            //emailService.sendVerification(user.getFirstName(),user.getEmail(),verificationUrl,ACCOUNT.getType());
            user.setEnabled(false);
            user.setNotLocked(true);
            //return the newly created user
            return user;
            //if any error throw exception with a proper message
        }
        catch(Exception exception){
            log.error(exception.getMessage());
            throw new ApiException(" An error occurred try again");
        }
    }

    @Override
    public Collection<User> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    private Integer getEmailCount(String email) {
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email",email),Integer.class);
    }

    private SqlParameterSource getSqlParameters(User user) {
        return new MapSqlParameterSource()
                .addValue("firstName",user.getFirstName())
                .addValue("lastName",user.getLastName())
                .addValue("email",user.getEmail())
                .addValue("password",encoder.encode(user.getPassword()));
    }

    private String getVerificationUrl(String key,String type){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify"+type+"/"+key).toUriString();
    }

    //is the method of UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);
        if(user == null){
            log.info("user not found in the database");
            throw new UsernameNotFoundException("user not found in the database");
        }
        else{
            log.info("user found in the database");
            return new UserPrincipal(user,roleRepository.getRoleByUserId(user.getId()).getPermission());
        }

    }

    @Override
    public User getUserByEmail(String email) {
        try {
            User user = jdbc.queryForObject(SELECT_BY_EMAIL_QUERY, Map.of("email", email), new UserRowMapper());
            return user;
        }
        catch(EmptyResultDataAccessException exception){
            log.error(exception.getMessage());
            throw new ApiException("No User Found by Email "+email);
        }
        catch(Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("An error occurred.Please try again");
        }
    }

    @Override
    public void sendVerificationCode(UserDTO userDTO) {
        String expirationDate = format(addDays(new Date(),1),DATE_FORMAT);
        String verificationCode = randomAlphabetic(8).toUpperCase();
        try {
            jdbc.update(DELETE_VERIFICATION_CODE_BY_USER_ID, Map.of("id", userDTO.getId()));
            jdbc.update(INSERT_VERIFICATION_CODE_QUERY, Map.of("userId", userDTO.getId(),"code",verificationCode,"expirationDate",expirationDate));
            sendSMS(userDTO.getPhone(),"From: Server \n Verification code\n "+verificationCode);
        }
        catch(Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("An error occurred.Please try again");
        }
    }
}
