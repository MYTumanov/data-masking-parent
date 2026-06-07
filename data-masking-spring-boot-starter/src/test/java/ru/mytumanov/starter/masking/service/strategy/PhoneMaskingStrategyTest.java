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
 * Тесты для стратегии маскирования номеров телефонов
 */
public class PhoneMaskingStrategyTest {
    private PhoneMaskingStrategy strategy;
    private MaskRule defaultRule;

    @BeforeEach
    void setUp() {
        strategy = new PhoneMaskingStrategy();
        defaultRule = new MaskRule();
    }

    @ParameterizedTest
    @DisplayName("Успешное маскирование номеров телефонов с настройками по умолчанию")
    @CsvSource({
            "+79254449922,      +79***9922",
            "+229254449922,     +229***9922",
            "+3339254449922,    +333***9922",
            "+3331234,          +3***234"

    })
    void shouldMaskPhoneWithDefaultSettings(String input, String expected) {
        String result = strategy.mask(input, defaultRule);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @DisplayName("Успешное маскирование номеров телефонов с сохранением длины")
    @CsvSource({
            "+79254449922,      +79*****9922",
            "+229254449922,     +229*****9922",
            "+3339254449922,    +333******9922",
            "+3331234,          +3***234"
    })
    void shouldMaskPhoneWithKeepLengthTrue(String input, String expected) {
        defaultRule.setKeepLength(true);

        String result = strategy.mask(input, defaultRule);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Успешное маскирование номеров телефонов с катомным символом маскировки")
    void shouldMaskPhoneWithCustomMaskChar() {
        defaultRule.setMaskChar("#");
        String phone = "+79254449922";
        String planResult = "+79###9922";

        String result = strategy.mask(phone, defaultRule);

        assertEquals(planResult, result);
    }

    @ParameterizedTest
    @DisplayName("Обработка пустых строк и некорректных номером телефона (без падений)")
    @ValueSource(strings = {
            "",
            "+",
            "+1",
            "+123456",
            "+7123ttt9922"
    })
    void shouldHandleEdgeCases(String input) {
        String result = strategy.mask(input, defaultRule);

        assertEquals(input, result);
    }
}
