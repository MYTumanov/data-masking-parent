package ru.mytumanov.starter.masking.service.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import ru.mytumanov.starter.masking.config.MaskingProperties.MaskRule;

/**
 * Тесты для стратегии маскирования ФИО (Full Name)
 */
public class FullNameMaskingStrategyTest {
    private FullNameMaskingStrategy strategy;
    private MaskRule defaultRule;

    @BeforeEach
    void setUp() {
        strategy = new FullNameMaskingStrategy();
        defaultRule = new MaskRule();
    }

    @ParameterizedTest
    @DisplayName("Успешное маскирование ФИО с настройками по умолчанию (keepLength = false)")
    @CsvSource({
            "Ivanov Ivan Ivanovich, I***v I***n I***h",
            "John Doe,              J***n D***e",
            "Иванов Иван Иванович,  И***в И***н И***ч",
            "A B C,                 A*** B*** C***"
    })
    void shouldMaskFullNameWithDefaultSettings(String input, String expected) {
        String result = strategy.mask(input, defaultRule);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @DisplayName("Успешное маскирование ФИО с сохранением длины")
    @CsvSource({
            "Ivanov Ivan Ivanovich, I****v I**n I*******h",
            "John Doe,              J**n D*e",
            "Иванов Иван Иванович,  И****в И**н И******ч",
            "A B C,                 * * *"
    })
    void shouldMaskFullNameWithKeepLengthTrue(String input, String expected) {
        defaultRule.setKeepLength(true);
        String result = strategy.mask(input, defaultRule);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Успешное маскирование ФИО с кастомным символом маскирования")
    void shouldMaskFullNameWithCustomMaskChar() {
        defaultRule.setMaskChar("#");
        String input = "Ivanov Ivan";
        String expected = "I###v I###n";
        String result = strategy.mask(input, defaultRule);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Сохранение оригинальных пробельных символов (табы, переносы, множественные пробелы)")
    void shouldPreserveOriginalWhitespaceCharacters() {
        String input = "Ivanov  \t Ivan \n Ivanovich";
        String expected = "I***v  \t I***n \n I***h";
        String result = strategy.mask(input, defaultRule);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @DisplayName("Маскирование коротких слов с keepLength = false")
    @CsvSource({
            "A,     A***",
            "Io,    I***",
            "Joe,   J***e"
    })
    void shouldMaskShortWords(String input, String expected) {
        String result = strategy.mask(input, defaultRule);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @DisplayName("Маскирование коротких слов с keepLength = true")
    @CsvSource({
            "'A',     '*'",
            "'Io',    'I*'",
            "'Joe',   'J*e'"
    })
    void shouldMaskShortWordsWithKeepLengthTrue(String input, String expected) {
        defaultRule.setKeepLength(true);
        String result = strategy.mask(input, defaultRule);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @DisplayName("Обработка пустых, null или состоящих только из пробелов строк")
    @ValueSource(strings = {
            "",
            "   ",
            "\t\n"
    })
    void shouldHandleEdgeCases(String input) {
        String result = strategy.mask(input, defaultRule);
        assertEquals(input, result);
    }
}
