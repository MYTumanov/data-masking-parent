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
 * Тесты для стратегии маскирования email
 */
public class EmailMaskingStrategyTest {
    private EmailMaskingStrategy strategy;
    private MaskRule defaultRule;

    @BeforeEach
    void setUp() {
        strategy = new EmailMaskingStrategy();
        defaultRule = new MaskRule();
    }

    @ParameterizedTest
    @DisplayName("Успешное маскирование email с настройками по умолчанию")
    @CsvSource({
            "mymailverysensitive@mail.ru, m***e@mail.ru",
            "alex@domain.com,             a***@domain.com",
            "id@service.ru,               i***@service.ru",
            "a@work.org,                  a***@work.org"
    })
    void shouldMaskEmailWithDefaultSettings(String input, String expected) {
        String result = strategy.mask(input, defaultRule);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @DisplayName("Успешное маскирование email с сохранением длины")
    @CsvSource({
            "mymailverysensitive@mail.ru, m*****************e@mail.ru",
            "alex@domain.com,             a**x@domain.com",
            "ale@domain.com,              a*e@domain.com",
            "id@service.ru,               i*@service.ru",
            "a@work.org,                  *@work.org"
    })
    void shouldMaskEmailWithKeepLengthTrue(String input, String expected) {
        defaultRule.setKeepLength(true);

        String result = strategy.mask(input, defaultRule);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Кастомный символ маскирования")
    void shouldMaskEmailWithCustomMaskChar() {
        defaultRule.setMaskChar("x");
        defaultRule.setKeepLength(false);
        String email = "mymailverysensitive@mail.ru";

        String result = strategy.mask(email, defaultRule);

        assertEquals("mxxxe@mail.ru", result);
    }

    @ParameterizedTest
    @DisplayName("Обработка пустых строк и некорректных email (без падений)")
    @ValueSource(strings = {
            "",
            "test@",
            "@test.com",
            "testtest.com",
            "plaintext"
    })
    void shouldHandleEdgeCases(String input) {
        String result = strategy.mask(input, defaultRule);

        assertEquals(input, result);
    }
}
