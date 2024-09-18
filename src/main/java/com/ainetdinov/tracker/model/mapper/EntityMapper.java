package com.ainetdinov.tracker.model.mapper;

import com.ainetdinov.tracker.model.dto.EntityDto;
import com.ainetdinov.tracker.model.entity.Source;
import com.ainetdinov.tracker.model.request.RequestEntity;

public interface EntityMapper<E extends Source, D extends EntityDto, R extends RequestEntity> {

    E toEntity(R request);

    D toDto(E entity);
}
