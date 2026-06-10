package ru.mytumanov.starter.masking.config;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.AnnotationIntrospector;

import ru.mytumanov.starter.masking.service.MaskingService;
import ru.mytumanov.starter.masking.service.strategy.EmailMaskingStrategy;
import ru.mytumanov.starter.masking.service.strategy.MaskingStrategy;
import ru.mytumanov.starter.masking.jakson.MaskingJacksonAnnotationIntrospector;

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

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer maskingJacksonCustomizer(MaskingService maskingService) {
        return builder -> builder.annotationIntrospector(defaultIntrospector -> {
            if (defaultIntrospector == null) {
                return new MaskingJacksonAnnotationIntrospector(maskingService);
            }
            return AnnotationIntrospector.pair(
                    new MaskingJacksonAnnotationIntrospector(maskingService), defaultIntrospector);
        });
    }
}