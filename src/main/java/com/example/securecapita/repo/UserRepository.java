package com.example.securecapita.repo;


import com.example.securecapita.dto.UserDTO;
import com.example.securecapita.model.User;

import java.util.Collection;

public interface UserRepository<T extends User> {

    /* Basic CRUD Operations */
    T create(T data);
    Collection<T> list(int page,int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);

    User getUserByEmail(String email);

    void sendVerificationCode(UserDTO userDTO);
}
