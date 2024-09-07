package com.ainetdinov.tracker.model.mapper;

import com.ainetdinov.tracker.model.dto.LabelDto;
import com.ainetdinov.tracker.model.entity.Label;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LabelMapper {

    LabelMapper INSTANCE = Mappers.getMapper(LabelMapper.class);

    Label toEntity(LabelDto labelDto);

    LabelDto toDto(Label label);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Label partialUpdate(LabelDto labelDto, @MappingTarget Label label);
}