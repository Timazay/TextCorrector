package by.timofeyzaytsev.textcorrector.features.textpolish.find_task;

import by.timofeyzaytsev.textcorrector.infrastructure.entity.TextPolishTask;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FindTextPolishTaskMapper {

    FindTextPolishTaskResponse toFindCorrectionTaskResponse(TextPolishTask task);
}
