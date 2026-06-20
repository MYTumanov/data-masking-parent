package ru.mytumanov.starter.masking.service.strategy;

import java.util.regex.Pattern;
import ru.mytumanov.starter.masking.config.MaskingProperties.MaskRule;
import ru.mytumanov.starter.masking.model.MaskType;

/**
 * Стратегия маскирования PAN (номер банковской карты)
 */
public class PanMaskingStrategy implements MaskingStrategy {
    private static final Pattern PAN_PATTERN = Pattern.compile("^[0-9]{13,19}$");
    private static final int DEFAULT_MASK_LENGTH = 6;

    @Override
    public MaskType getType() {
        return MaskType.PAN;
    }

    @Override
    public String mask(String source, MaskRule properties) {
        if (source == null || source.isBlank()) {
            return source;
        }

        if (!PAN_PATTERN.matcher(source).matches()) {
            return source;
        }

        int len = source.length();
        String firstSix = source.substring(0, 6);
        String lastFour = source.substring(len - 4);
        String maskChar = properties.getMaskChar();

        int repeatCount = properties.isKeepLength() ? (len - 10) : DEFAULT_MASK_LENGTH;
        String maskString = maskChar.repeat(repeatCount);

        return firstSix + maskString + lastFour;
    }
}
