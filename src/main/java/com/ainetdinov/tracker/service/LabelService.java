package com.ainetdinov.tracker.service;

import com.ainetdinov.tracker.command.get.GetLabelByName;
import com.ainetdinov.tracker.command.remove.RemoveLabel;
import com.ainetdinov.tracker.command.update.UpdateLabel;
import com.ainetdinov.tracker.command.validate.ValidateDeletionAvailability;
import com.ainetdinov.tracker.model.dto.LabelDto;
import com.ainetdinov.tracker.model.entity.Label;
import com.ainetdinov.tracker.model.mapper.LabelMapper;
import com.ainetdinov.tracker.model.request.LabelRequest;
import com.ainetdinov.tracker.repository.LabelRepository;
import lombok.extern.log4j.Log4j2;

import java.util.ResourceBundle;

@Log4j2
public class LabelService extends EntityService<Label, LabelDto, LabelRequest> {

    public LabelService(LabelRepository labelRepository, LabelMapper mapper, ResourceBundle messages) {
        super(labelRepository, mapper, messages);
    }

    public Label getLabelByName(LabelRequest request) {
        log.debug("Command: GetLabelByName\tget label with name {}", request.getLabel());
        return executeCommand(new GetLabelByName((LabelRepository) repository, request.getLabel()));
    }

    public boolean validateLabelDeletionAvailability(LabelRequest request) {
        return executeCommand(new ValidateDeletionAvailability((LabelRepository) repository, request));
    }

    @Override
    public LabelDto updateEntity(LabelRequest label) {
        log.debug("Command: UpdateLabel\tupdate label {}", label.getLabel());
        return executeCommand(new UpdateLabel((LabelRepository) repository, (LabelMapper) mapper, label));
    }

    @Override
    public void deleteEntity(LabelRequest label) {
        log.debug("Command: RemoveLabel\tdelete label {}", label.getLabel());
        executeCommand(new RemoveLabel(repository, (LabelMapper) mapper, label));
    }

}
