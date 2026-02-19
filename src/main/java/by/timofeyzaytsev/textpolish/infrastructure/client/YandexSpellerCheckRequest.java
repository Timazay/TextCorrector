package by.timofeyzaytsev.textpolish.infrastructure.client;

import java.util.List;

public record YandexSpellerCheckRequest(List<String> text, String lang, int options) {
}
