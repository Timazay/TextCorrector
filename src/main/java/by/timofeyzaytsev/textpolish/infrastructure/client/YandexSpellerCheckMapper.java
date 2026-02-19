package by.timofeyzaytsev.textpolish.infrastructure.client;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface YandexSpellerCheckMapper {

    @Mapping(target = "text", source = "textArray")
    @Mapping(target = "lang", source = "language")
    YandexSpellerCheckRequest toYandexSpellerCheckRequest(String language, List<String> textArray, int options);
}
