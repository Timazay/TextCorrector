package by.timofeyzaytsev.textcorrector.features.textpolish.create_task;

import by.timofeyzaytsev.textcorrector.infrastructure.entity.TextPolishTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreateTextPolishTaskMapper {

    @Mapping(target = "status",
            expression = "java(by.timofeyzaytsev.textcorrector.infrastructure.entity.enums.TextPolishTaskStatus.NEW_TASK)")
    TextPolishTask toCorrectionTask(CreateTextPolishTaskRequest request);
}
