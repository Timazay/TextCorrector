package by.timofeyzaytsev.textcorrector.features.textpolish.task_polish;

import java.util.List;

public record YandexSpellerCheckRequest(List<String> text, String lang, int options) {
}
