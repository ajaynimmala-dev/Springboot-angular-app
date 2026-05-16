package com.example.securecapita.dtomapper;

import com.example.securecapita.dto.UserDTO;
import com.example.securecapita.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {

    public static UserDTO fromUser(User user){
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        return userDTO;
    }

    public static User toUser(UserDTO userDTO){

        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        return user;
    }
}
