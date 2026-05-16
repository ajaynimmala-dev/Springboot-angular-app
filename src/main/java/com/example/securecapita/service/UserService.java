package com.example.securecapita.service;

import com.example.securecapita.dto.UserDTO;
import com.example.securecapita.model.User;

public interface UserService {

    UserDTO createUser(User user);
}
