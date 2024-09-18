package com.ainetdinov.tracker.service;

import com.ainetdinov.tracker.command.get.GetUserByUsername;
import com.ainetdinov.tracker.command.validate.ValidateUserPresence;
import com.ainetdinov.tracker.command.validate.ValidateUsernamePresence;
import com.ainetdinov.tracker.model.dto.UserDto;
import com.ainetdinov.tracker.model.entity.User;
import com.ainetdinov.tracker.model.mapper.UserMapper;
import com.ainetdinov.tracker.model.request.UserRequest;
import com.ainetdinov.tracker.repository.UserRepository;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;
import java.util.ResourceBundle;

@Log4j2
public class UserService extends EntityService<User, UserDto, UserRequest> {

    public UserService(UserRepository userRepository, UserMapper mapper, ResourceBundle messages) {
        super(userRepository, mapper, messages);
    }

    public UserDto getUserByUsername(UserRequest userRequest) {
        log.debug("Command: GetUserByUsername\tget user with username {}", userRequest.getUsername());
        return executeCommand(new GetUserByUsername((UserRepository) repository, (UserMapper) mapper, userRequest));
    }

    public boolean validateUserPresence(UserRequest userRequest) {
        log.debug("Command: ValidateUserPresence\tcheck if user with entered credentials exists {}:{}", userRequest.getUsername(), userRequest.getPassword());
        return executeCommand(new ValidateUserPresence((UserRepository) repository, userRequest));
    }

    public boolean validatePasswordLength(UserRequest userRequest) {
        log.debug("Validate password presence:\n{}", userRequest.getPassword());
        var password = userRequest.getPassword();
        return Objects.nonNull(password) && !password.trim().isEmpty();
    }

    public boolean validateUsernameAvailability(UserRequest userRequest) {
        log.debug("Command: ValidateUsernamePresence\tcheck if entered username {} is available", userRequest.getUsername());
        return !executeCommand(new ValidateUsernamePresence((UserRepository) repository, userRequest));
    }
}
