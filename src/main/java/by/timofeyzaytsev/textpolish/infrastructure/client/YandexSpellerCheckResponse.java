package by.timofeyzaytsev.textpolish.infrastructure.client;

import lombok.Builder;

import java.util.List;

@Builder
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
