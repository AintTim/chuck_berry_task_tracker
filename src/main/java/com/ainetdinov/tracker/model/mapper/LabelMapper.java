package com.ainetdinov.tracker.model.mapper;

import com.ainetdinov.tracker.model.dto.LabelDto;
import com.ainetdinov.tracker.model.entity.Label;
import com.ainetdinov.tracker.model.request.LabelRequest;
import com.ainetdinov.tracker.repository.LabelRepository;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LabelMapper extends EntityMapper<Label, LabelDto, LabelRequest> {

    LabelMapper INSTANCE = Mappers.getMapper(LabelMapper.class);

    @Override
    Label toEntity(LabelRequest labelRequest);

    @Override
    LabelDto toDto(Label label);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Label partialUpdate(LabelDto labelDto, @MappingTarget Label label);
}