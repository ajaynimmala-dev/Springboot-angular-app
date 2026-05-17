package com.example.securecapita.service;

import com.example.securecapita.dto.UserDTO;
import com.example.securecapita.model.User;
import jakarta.validation.constraints.NotEmpty;

public interface UserService {

    UserDTO createUser(User user);

    UserDTO getUserByEmail( String email);
}
