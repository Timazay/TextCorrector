package by.timofeyzaytsev.textcorrector.mapper;

import by.timofeyzaytsev.textcorrector.dto.request.CreateCorrectionTaskRequest;
import by.timofeyzaytsev.textcorrector.dto.request.YandexSpellCheckRequest;
import by.timofeyzaytsev.textcorrector.dto.response.FindCorrectionTaskResponse;
import by.timofeyzaytsev.textcorrector.entity.CorrectionTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CorrectionTaskMapper {

    @Mapping(target = "status",
            expression = "java(by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskStatus.NEW_TASK)")
    CorrectionTask toCorrectionTask(CreateCorrectionTaskRequest request);

    @Mapping(target = "text", source = "textArray")
    @Mapping(target = "lang", source = "task.language")
    YandexSpellCheckRequest toYandexSpellCheckRequest(CorrectionTask task, List<String> textArray, int options);

    FindCorrectionTaskResponse toFindCorrectionTaskResponse(CorrectionTask task);

    @Mapping(target = "status",
            expression = "java(by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskStatus.FINISHED)")
    void toCorrectionTask(@MappingTarget CorrectionTask task, String text);
}


