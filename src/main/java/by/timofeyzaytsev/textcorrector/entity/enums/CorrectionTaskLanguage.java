package by.timofeyzaytsev.textcorrector.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CorrectionTaskLanguage {
    EN, RU;

    @JsonCreator
    public static CorrectionTaskLanguage fromString(String value) {
        if (value == null)
            return null;

        String normalized = value.trim().toUpperCase();

        return CorrectionTaskLanguage.valueOf(normalized);
    }
}
