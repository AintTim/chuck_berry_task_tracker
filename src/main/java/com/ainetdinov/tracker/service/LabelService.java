package com.ainetdinov.tracker.service;

import com.ainetdinov.tracker.command.remove.RemoveLabel;
import com.ainetdinov.tracker.command.update.UpdateLabel;
import com.ainetdinov.tracker.model.dto.LabelDto;
import com.ainetdinov.tracker.model.entity.Label;
import com.ainetdinov.tracker.model.mapper.LabelMapper;
import com.ainetdinov.tracker.repository.LabelRepository;

import java.util.ArrayList;
import java.util.List;

public class LabelService extends EntityService<Label> {
    private final LabelMapper labelMapper;

    public LabelService(LabelRepository labelRepository, LabelMapper labelMapper) {
        super(labelRepository);
        this.labelMapper = labelMapper;
    }

    public List<LabelDto> getLabels() {
        var labels = getEntities();
        return toDtoList(labels);
    }

    public LabelDto getLabel(Long id) {
        return labelMapper.toDto(getEntityById(id));
    }

    @Override
    public LabelDto updateEntity(Label label) {
        return labelMapper.toDto(executeCommand(new UpdateLabel(repository, label)));
    }

    @Override
    public void deleteEntity(Long id) {
        executeCommand(new RemoveLabel(repository, id));
    }

    private List<LabelDto> toDtoList(List<Label> labels) {
        List<LabelDto> dtoList = new ArrayList<>();
        for (var label : labels) {
            dtoList.add(labelMapper.toDto(label));
        }
        return dtoList;
    }
}
