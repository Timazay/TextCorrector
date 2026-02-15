package by.timofeyzaytsev.textcorrector.feignclient;

import by.timofeyzaytsev.textcorrector.dto.response.YandexSpellCheckResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "yandexSpeller")
public interface ExternalYandexSpellerApiClient {

    @PostMapping
    List<List<YandexSpellCheckResponse>> checkText(@RequestParam String text, @RequestParam String lang);
}
