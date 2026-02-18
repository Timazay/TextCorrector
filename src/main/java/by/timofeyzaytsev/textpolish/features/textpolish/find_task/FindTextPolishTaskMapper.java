package by.timofeyzaytsev.textpolish.features.textpolish.find_task;

import by.timofeyzaytsev.textpolish.infrastructure.entity.TextPolishTask;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FindTextPolishTaskMapper {

    FindTextPolishTaskResponse toFindCorrectionTaskResponse(TextPolishTask task);
}
