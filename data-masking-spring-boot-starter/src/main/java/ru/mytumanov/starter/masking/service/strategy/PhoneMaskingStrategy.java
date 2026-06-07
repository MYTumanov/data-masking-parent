package ru.mytumanov.starter.masking.service.strategy;

import java.util.regex.Pattern;

import ru.mytumanov.starter.masking.config.MaskingProperties.MaskRule;
import ru.mytumanov.starter.masking.model.MaskType;

/**
 * Стратегия маскирования номеров телефонов
 */
public class PhoneMaskingStrategy implements MaskingStrategy {
    private static final float MASK_RATIO = 0.4f;
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{7,15}$");

    @Override
    public MaskType getType() {
        return MaskType.PHONE;
    }

    @Override
    public String mask(String source, MaskRule properties) {
        if (source == null || source.isBlank()) {
            return source;
        }

        if (!PHONE_PATTERN.matcher(source).matches()) {
            return source;
        }

        int len = source.length();
        int maskLength = Math.round(len * MASK_RATIO);
        int maskStart = (len - maskLength) / 2;
        int maskEnd = maskStart + maskLength;
        int repeatCount = properties.isKeepLength() ? maskLength : 3;
        String maskChar = properties.getMaskChar();

        String maskString = maskChar.repeat(repeatCount);
        return source.substring(0, maskStart) + maskString + source.substring(maskEnd);
    }
}
