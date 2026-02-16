package by.timofeyzaytsev.textcorrector.dto.request;

import java.util.List;

public record YandexSpellerCheckRequest(List<String> text, String lang, int options) {
}
