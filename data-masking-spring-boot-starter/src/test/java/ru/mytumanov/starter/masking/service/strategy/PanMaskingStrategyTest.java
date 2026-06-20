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
 * Тесты для стратегии маскирования PAN
 */
public class PanMaskingStrategyTest {
    private PanMaskingStrategy strategy;
    private MaskRule defaultRule;

    @BeforeEach
    void setUp() {
        strategy = new PanMaskingStrategy();
        defaultRule = new MaskRule();
    }

    @ParameterizedTest
    @DisplayName("Успешное маскирование PAN с настройками по умолчанию")
    @CsvSource({
            "1234567890123456,    123456******3456",
            "1234567890123,       123456******0123",
            "1234567890123456789, 123456******6789"
    })
    void shouldMaskPanWithDefaultSettings(String input, String expected) {
        String result = strategy.mask(input, defaultRule);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @DisplayName("Успешное маскирование PAN с сохранением длины")
    @CsvSource({
            "1234567890123456,    123456******3456",
            "1234567890123,       123456***0123",
            "1234567890123456789, 123456*********6789"
    })
    void shouldMaskPanWithKeepLengthTrue(String input, String expected) {
        defaultRule.setKeepLength(true);
        String result = strategy.mask(input, defaultRule);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Успешное маскирование PAN с кастомным символом маскирования")
    void shouldMaskPanWithCustomMaskChar() {
        defaultRule.setMaskChar("X");
        String input = "1234567890123456";
        String expected = "123456XXXXXX3456";
        String result = strategy.mask(input, defaultRule);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @DisplayName("Обработка пустых строк и некорректных PAN (без падений)")
    @ValueSource(strings = {
            "",
            "123456789012",       // Слишком короткий (12 символов)
            "12345678901234567890", // Слишком длинный (20 символов)
            "123456789012345a",    // Содержит букву
            "1234-5678-9012-3456", // Содержит дефисы
            "1234 5678 9012 3456"  // Содержит пробелы
    })
    void shouldHandleEdgeCases(String input) {
        String result = strategy.mask(input, defaultRule);
        assertEquals(input, result);
    }
}
