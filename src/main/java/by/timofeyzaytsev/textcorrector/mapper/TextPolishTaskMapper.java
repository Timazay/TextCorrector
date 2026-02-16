package by.timofeyzaytsev.textcorrector.mapper;

import by.timofeyzaytsev.textcorrector.dto.request.CreateTextPolishTaskRequest;
import by.timofeyzaytsev.textcorrector.dto.request.YandexSpellerCheckRequest;
import by.timofeyzaytsev.textcorrector.dto.response.FindTextPolishTaskResponse;
import by.timofeyzaytsev.textcorrector.entity.TextPolishTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TextPolishTaskMapper {

    @Mapping(target = "status",
            expression = "java(by.timofeyzaytsev.textcorrector.entity.enums.TextPolishTaskStatus.NEW_TASK)")
    TextPolishTask toCorrectionTask(CreateTextPolishTaskRequest request);

    @Mapping(target = "text", source = "textArray")
    @Mapping(target = "lang", source = "language")
    YandexSpellerCheckRequest toYandexSpellerCheckRequest(String language, List<String> textArray, int options);

    FindTextPolishTaskResponse toFindCorrectionTaskResponse(TextPolishTask task);

    @Mapping(target = "status",
            expression = "java(by.timofeyzaytsev.textcorrector.entity.enums.TextPolishTaskStatus.FINISHED)")
    void toCorrectionTask(@MappingTarget TextPolishTask task, String text);
}


