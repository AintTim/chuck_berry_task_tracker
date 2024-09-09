package com.ainetdinov.tracker.service;

import com.ainetdinov.tracker.command.generic.UpdateCommand;
import com.ainetdinov.tracker.command.remove.RemoveUser;
import com.ainetdinov.tracker.model.dto.UserDto;
import com.ainetdinov.tracker.model.entity.User;
import com.ainetdinov.tracker.model.mapper.UserMapper;
import com.ainetdinov.tracker.repository.UserRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserService extends EntityService<User> {
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        super(userRepository);
        this.userMapper = userMapper;
    }

    public List<UserDto> getUsers() {
        var users = getEntities();
        return toDtoList(users);
    }

    public UserDto getUser(Long id) {
        return userMapper.toDto(getEntityById(id));
    }

    @Override
    public Serializable updateEntity(User user) {
        return userMapper.toDto(executeCommand(new UpdateCommand<>(repository, user)));
    }

    @Override
    public void deleteEntity(Long id) {
        executeCommand(new RemoveUser(repository, id));
    }

    private List<UserDto> toDtoList(List<User> users) {
        List<UserDto> dtoList = new ArrayList<>();
        for (var user : users) {
            dtoList.add(userMapper.toDto(user));
        }
        return dtoList;
    }
}
