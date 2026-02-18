package by.timofeyzaytsev.textpolish.features.textpolish.create_task;

import by.timofeyzaytsev.textpolish.infrastructure.entity.TextPolishTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreateTextPolishTaskMapper {

    @Mapping(target = "status",
            expression = "java(by.timofeyzaytsev.textpolish.infrastructure.entity.enums.TextPolishTaskStatus.NEW_TASK)")
    TextPolishTask toTextPolishTask(CreateTextPolishTaskRequest request);
}
