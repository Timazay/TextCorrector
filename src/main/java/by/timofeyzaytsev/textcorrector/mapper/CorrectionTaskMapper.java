package by.timofeyzaytsev.textcorrector.mapper;

import by.timofeyzaytsev.textcorrector.dto.request.CreateTextCorrectionRequest;
import by.timofeyzaytsev.textcorrector.dto.request.YandexSpellCheckRequest;
import by.timofeyzaytsev.textcorrector.entity.CorrectionTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CorrectionTaskMapper {

    @Mapping(target = "status",
            expression = "java(by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskStatus.NEW_TASK)")
    CorrectionTask toCorrectionTask(CreateTextCorrectionRequest request);

    @Mapping(target = "text", source = "textArray")
    @Mapping(target = "lang", source = "task.language")
    YandexSpellCheckRequest toYandexSpellCheckRequest(CorrectionTask task, List<String> textArray, int options);
}
