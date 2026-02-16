package by.timofeyzaytsev.textcorrector.client;

import by.timofeyzaytsev.textcorrector.dto.response.YandexSpellCheckResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "yandexSpeller")
public interface ExternalYandexSpellerApiClient {

    @PostMapping(value = "/checkTexts",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    List<List<YandexSpellCheckResponse>> checkText(@RequestParam String text, @RequestParam String lang, @RequestParam int options);
}
