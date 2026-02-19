package by.timofeyzaytsev.textpolish.feature.textpolish.common;

import by.timofeyzaytsev.textpolish.features.textpolish.common.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class StringUtilsTest {

    @Test
    void splitIntoBlocks_WhenNullInput_ShouldReturnEmptyList() {
        // Act and Arrange
        List<String> result = StringUtils.splitIntoBlocks(null, 10);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void splitIntoBlocks_WhenTextFitsInBlockSize_ShouldReturnSingleBlock() {
        // Arrange
        String text = "This is a short text.";
        int blockSize = 100;

        // Act
        List<String> result = StringUtils.splitIntoBlocks(text, blockSize);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(text);
    }

    @Test
    void splitIntoBlocks_WhenMultipleSentencesWithBlockSizeLimit_ShouldSplitBySentences() {
        // Arrange
        String text = "First sentence. Second sentence! Third sentence? Fourth one.";
        int blockSize = 20;

        // Act
        List<String> result = StringUtils.splitIntoBlocks(text, blockSize);

        // Assert
        assertThat(result).containsExactly(
                "First sentence. ",
                "Second sentence! ",
                "Third sentence? ",
                "Fourth one."
        );
    }

    @Test
    void splitIntoBlocks_WhenVerySmallBlockSizeWithSpaces_ShouldSplitCorrectlyPreservingSpaces() {
        // Arrange
        String text = "Hello world";
        int blockSize = 3;

        // Act
        List<String> result = StringUtils.splitIntoBlocks(text, blockSize);

        // Assert
        assertThat(result).containsExactly(
                "Hel",
                "lo",
                " ",
                "wor",
                "ld"
        );

        String joined = String.join("", result);
        assertThat(joined).isEqualTo(text);
    }

    @Test
    void containsDigits_WhenNullInput_ShouldReturnFalse() {
        // Act and Arrange
        boolean result = StringUtils.containsDigits(null);

        // Assert
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "text with digit 1",
            "123",
            "digit at the end 5",
            "7 at start",
            "mixed text with 42 number",
            "multiple 123 digits 456",
            "special!@# 789 chars",
            "1234567890"
    })
    void containsDigits_WhenTextWithDigits_ShouldReturnTrue(String text) {
        // Act
        boolean result = StringUtils.containsDigits(text);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void containsUrl_WhenNullInput_ShouldReturnFalse() {
        // Act and Arrange
        boolean result = StringUtils.containsUrl(null);

        // Assert
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Check http://example.com",
            "https://secure-site.com",
            "Visit www.example.com",
            "text with http://example.com and more text",
            "multiple urls: http://first.com and https://second.com",
            "url at end http://example.com",
            "http://example.com at start",
            "www.example.com/path",
            "check this: https://subdomain.example.co.uk/path?param=1",
            "domain.com",
            "example.org",
            "site.net",
            "sub.domain.com",
            "domain.com/path",
            "domain.com/file.html",
            "domain.com ",
            "domain.com\n",
            "Check domain.com for details"
    })
    void containsUrl_WhenTextWithUrl_ShouldReturnTrue(String text) {
        // Act
        boolean result = StringUtils.containsUrl(text);

        // Assert
        assertThat(result).isTrue();
    }
}
