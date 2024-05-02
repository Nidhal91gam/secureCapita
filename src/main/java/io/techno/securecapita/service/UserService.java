package io.techno.securecapita.service;

import io.techno.securecapita.domain.User;
import io.techno.securecapita.dto.UserDTO;

public interface UserService {

    UserDTO createUser(User user);
}
