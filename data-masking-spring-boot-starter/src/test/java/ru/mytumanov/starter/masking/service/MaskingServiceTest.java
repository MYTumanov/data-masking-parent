package ru.mytumanov.starter.masking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.mytumanov.starter.masking.config.MaskingProperties;
import ru.mytumanov.starter.masking.config.MaskingProperties.MaskRule;
import ru.mytumanov.starter.masking.model.MaskType;
import ru.mytumanov.starter.masking.service.strategy.EmailMaskingStrategy;
import ru.mytumanov.starter.masking.service.strategy.MaskingStrategy;

public class MaskingServiceTest {
    @Test
    @DisplayName("Должна делегировать вызов стратегии EMAIL")
    void shouldDelegateToEmailMaskingStrategy() {
        String rawValue = "test@mail.ru";
        String maskedValue = "t***t@mail.ru";

        MaskingStrategy emailMaskingStrategy = mock(EmailMaskingStrategy.class);
        when(emailMaskingStrategy.getType()).thenReturn(MaskType.EMAIL);
        when(emailMaskingStrategy.mask(eq(rawValue), any(MaskRule.class))).thenReturn(maskedValue);

        MaskingProperties properties = new MaskingProperties();

        MaskingService maskingService = new MaskingService(List.of(emailMaskingStrategy), properties);

        String result = maskingService.mask(rawValue, MaskType.EMAIL);

        assertEquals(maskedValue, result);
        verify(emailMaskingStrategy, times(1)).mask(eq(rawValue), any(MaskRule.class));
    }

    @Test
    @DisplayName("Должен безопасно вернуть оригинал, если стратегия для типа отсутствует (Защита от NPE)")
    void shouldReturnOriginalValueIfStrategyNotFound() {
        String secretData = "secrete_data";
        MaskingProperties properties = new MaskingProperties();
        MaskingService maskingService = new MaskingService(List.of(), properties);

        String result = maskingService.mask(secretData, MaskType.EMAIL);

        assertEquals(secretData, result);
    }

    @Test
    @DisplayName("Должен использовать кастомные настройки из MaskingProperties, если они заданы")
    void shouldApplyCustomRulesFromProperties() {
        MaskingProperties properties = new MaskingProperties();
        MaskingProperties.MaskRule rule = new MaskingProperties.MaskRule();
        rule.setKeepLength(true);
        rule.setMaskChar("#");
        properties.getRules().put(MaskType.EMAIL.name(), rule);

        MaskingStrategy strategy = mock(EmailMaskingStrategy.class);
        when(strategy.getType()).thenReturn(MaskType.EMAIL);

        MaskingService maskingService = new MaskingService(List.of(strategy), properties);
        maskingService.mask("any@mail.ru", MaskType.EMAIL);

        verify(strategy).mask(eq("any@mail.ru"),
                argThat(argRule -> "#".equals(argRule.getMaskChar()) && argRule.isKeepLength()));
    }
}
