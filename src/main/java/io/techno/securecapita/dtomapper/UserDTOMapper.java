package io.techno.securecapita.dtomapper;

import io.techno.securecapita.domain.User;
import io.techno.securecapita.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {

    public static UserDTO fromUser(User user){
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
    public static User fromUserDTO (UserDTO userDTO){
        User user = new User();
        BeanUtils.copyProperties(userDTO , user);
        return user;
    }


}
