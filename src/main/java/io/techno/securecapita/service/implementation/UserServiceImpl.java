package io.techno.securecapita.service.implementation;

import io.techno.securecapita.domain.User;
import io.techno.securecapita.dto.UserDTO;
import io.techno.securecapita.dtomapper.UserDTOMapper;
import io.techno.securecapita.repository.UserRepository;
import io.techno.securecapita.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;

    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper.fromUser(userRepository.create(user));
    }
}
