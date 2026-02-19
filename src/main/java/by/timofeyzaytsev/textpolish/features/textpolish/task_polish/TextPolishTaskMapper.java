package by.timofeyzaytsev.textpolish.features.textpolish.task_polish;

import by.timofeyzaytsev.textpolish.infrastructure.entity.TextPolishTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TextPolishTaskMapper {

    @Mapping(target = "status",
            expression = "java(by.timofeyzaytsev.textpolish.infrastructure.entity.enums.TextPolishTaskStatus.FINISHED)")
    void toFinishTask(@MappingTarget TextPolishTask task, String text);
}
