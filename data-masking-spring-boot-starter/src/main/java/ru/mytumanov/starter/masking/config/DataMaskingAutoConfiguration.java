package ru.mytumanov.starter.masking.config;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import ru.mytumanov.starter.masking.service.MaskingService;
import ru.mytumanov.starter.masking.service.strategy.EmailMaskingStrategy;
import ru.mytumanov.starter.masking.service.strategy.MaskingStrategy;

/**
 * Автоматическая конфигурация для маскирования данных
 */
@AutoConfiguration
@EnableConfigurationProperties(MaskingProperties.class)
@ConditionalOnProperty(prefix = "data-masking", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DataMaskingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MaskingStrategy emailMaskingStrategy() {
        return new EmailMaskingStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    public MaskingService maskingService(List<MaskingStrategy> strategies,
            MaskingProperties properties) {
        return new MaskingService(strategies, properties);
    }

    // // Регистрируем кастомный модуль в Jackson ObjectMapper приложения
    // @Bean
    // public com.fasterxml.jackson.databind.Module
    // jacksonMaskingModule(MaskingService maskingService) {
    // com.fasterxml.jackson.databind.module.SimpleModule module = new
    // com.fasterxml.jackson.databind.module.SimpleModule();
    // // Добавляем логику: если у поля типа String есть аннотация @Mask, Jackson
    // // применит наш сериализатор
    // module.addSerializer(String.class, new MaskingJacksonSerializer(null, null,
    // maskingService));
    // return module;
    // }
}