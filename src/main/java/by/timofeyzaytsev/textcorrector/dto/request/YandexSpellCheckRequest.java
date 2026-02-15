package by.timofeyzaytsev.textcorrector.dto.request;

import java.util.List;

public record YandexSpellCheckRequest(List<String> text, String lang, int options) {
}
