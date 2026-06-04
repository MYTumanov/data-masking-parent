package ru.mytumanov.starter.masking.service.strategy;

import ru.mytumanov.starter.masking.config.MaskingProperties;
import ru.mytumanov.starter.masking.model.MaskType;

/**
 * Стратегия маскирования данных.
 */
public interface MaskingStrategy {
    /**
     * Тип маскирования
     * 
     * @return Тип маскирования
     */
    MaskType getType();

    /**
     * Маскирование данных
     * 
     * @param source     Исходные данные
     * @param properties Параметры маскировки
     * @return Маскированные данные
     */
    String mask(String source, MaskingProperties.MaskRule properties);
}
