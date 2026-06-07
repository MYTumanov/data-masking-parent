package ru.mytumanov.starter.masking.service.strategy;

import ru.mytumanov.starter.masking.config.MaskingProperties.MaskRule;
import ru.mytumanov.starter.masking.model.MaskType;

/**
 * Стратегия маскирования номеров телефонов
 */
public class PhoneMaskingStrategy implements MaskingStrategy {
    @Override
    public MaskType getType() {
        return MaskType.PHONE;
    }

    @Override
    public String mask(String source, MaskRule properties) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mask'");
    }

}
