package by.timofeyzaytsev.textcorrector.features.textpolish.task_polish;

import by.timofeyzaytsev.textcorrector.infrastructure.entity.TextPolishTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ToPolishTaskMapper {

    @Mapping(target = "status",
            expression = "java(by.timofeyzaytsev.textcorrector.infrastructure.entity.enums.TextPolishTaskStatus.FINISHED)")
    void toCorrectionTask(@MappingTarget TextPolishTask task, String text);

    @Mapping(target = "text", source = "textArray")
    @Mapping(target = "lang", source = "language")
    YandexSpellerCheckRequest toYandexSpellerCheckRequest(String language, List<String> textArray, int options);
}
