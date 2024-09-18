package com.ainetdinov.tracker.model.mapper;

import com.ainetdinov.tracker.model.dto.UserDto;
import com.ainetdinov.tracker.model.entity.User;
import com.ainetdinov.tracker.model.request.UserRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends EntityMapper<User, UserDto, UserRequest> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Override
    User toEntity(UserRequest userRequest);

    @Override
    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);
}