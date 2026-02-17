package by.timofeyzaytsev.textcorrector.features.textpolish.task_polish;

import java.util.List;

public record YandexSpellerCheckResponse(
         int code,
         int pos,
         int row,
         int col,
         int len,
         String word,
         List<String> s
) {
}
