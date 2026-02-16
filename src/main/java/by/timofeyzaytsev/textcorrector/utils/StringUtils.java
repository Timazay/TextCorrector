package by.timofeyzaytsev.textcorrector.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtils {

    public static boolean containsDigits(String text) {
        return text != null && text.matches(".*\\d.*");
    }

    public static boolean containsUrl(String text) {
        if (text == null) return false;

        return text.contains("http://") ||
                text.contains("https://") ||
                text.contains("www.") ||
                text.matches(".*\\.[a-zA-Z]{2,3}([/\\s].*|$)");
    }

    /**
     * Разбивает текст на блоки указанного размера с сохранением целостности предложений
     * @param text исходный текст
     * @param blockSize максимальный размер блока в символах
     * @return список блоков текста
     */
    public static List<String> splitIntoBlocks(String text, int blockSize) {
        List<String> blocks = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return blocks;
        }

        List<String> sentences = splitSentencesKeepDelimiters(text);

        StringBuilder currentBlock = new StringBuilder();

        for (String sentence : sentences) {
            if (currentBlock.length() + sentence.length() <= blockSize) {
                currentBlock.append(sentence);
            } else {
                if (currentBlock.length() > 0) {
                    blocks.add(currentBlock.toString());
                    currentBlock = new StringBuilder();
                }

                if (sentence.length() > blockSize) {
                    List<String> parts = splitLongSentenceWithSpaces(sentence, blockSize);
                    blocks.addAll(parts);
                } else {
                    currentBlock.append(sentence);
                }
            }
        }

        if (currentBlock.length() > 0) {
            blocks.add(currentBlock.toString());
        }

        return blocks;
    }

    /**
     * Разбивает текст на предложения, сохраняя разделители и пробелы
     */
    private static List<String> splitSentencesKeepDelimiters(String text) {
        List<String> sentences = new ArrayList<>();

        Pattern pattern = Pattern.compile("([^.!?]+[.!?]+)(\\s*)");

        Matcher matcher = pattern.matcher(text);
        int lastEnd = 0;

        while (matcher.find()) {
            String sentence = matcher.group(1);
            String spaces = matcher.group(2);

            String fullSentence = sentence + spaces;
            sentences.add(fullSentence);

            lastEnd = matcher.end();
        }

        if (lastEnd < text.length()) {
            String remaining = text.substring(lastEnd);
            if (!remaining.isEmpty()) {
                sentences.add(remaining);
            }
        }

        return sentences;
    }

    /**
     * Разбивает длинное предложение на части с учетом пробелов
     */
    private static List<String> splitLongSentenceWithSpaces(String sentence, int maxLength) {
        List<String> parts = new ArrayList<>();

        String[] words = sentence.split("(?<=\\s)|(?=\\s)");

        StringBuilder currentPart = new StringBuilder();

        for (String word : words) {
            if (currentPart.length() + word.length() <= maxLength) {
                currentPart.append(word);
            } else {
                if (currentPart.length() > 0) {
                    parts.add(currentPart.toString());
                    currentPart = new StringBuilder();
                }

                if (word.length() > maxLength) {
                    parts.addAll(splitByCharacters(word, maxLength));
                } else {
                    currentPart.append(word);
                }
            }
        }

        if (currentPart.length() > 0) {
            parts.add(currentPart.toString());
        }

        return parts;
    }

    /**
     * Разбивает текст по символам (крайний случай)
     */
    private static List<String> splitByCharacters(String text, int maxLength) {
        List<String> parts = new ArrayList<>();

        for (int i = 0; i < text.length(); i += maxLength) {
            int end = Math.min(i + maxLength, text.length());
            parts.add(text.substring(i, end));
        }

        return parts;
    }
}
