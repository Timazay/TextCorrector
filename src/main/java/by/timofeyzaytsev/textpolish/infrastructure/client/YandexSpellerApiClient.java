package by.timofeyzaytsev.textpolish.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.List;

@FeignClient(name = "yandexSpeller")
interface YandexSpellerApiClient {

    @PostMapping(value = "/checkTexts",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    List<List<YandexSpellerCheckResponse>> checkText(@RequestPart("text") List<String> text,
                                                     @RequestPart("lang") String language,
                                                     @RequestPart("options") int options);
}
