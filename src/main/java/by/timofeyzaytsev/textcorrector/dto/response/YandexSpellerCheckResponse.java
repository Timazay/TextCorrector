package by.timofeyzaytsev.textcorrector.dto.response;

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
