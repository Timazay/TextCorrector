package by.timofeyzaytsev.textpolish.infrastructure.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Language {
    EN, RU;

    @JsonCreator
    public static Language fromString(String value) {
        if (value == null)
            return null;

        String normalized = value.trim().toUpperCase();

        return Language.valueOf(normalized);
    }
}
