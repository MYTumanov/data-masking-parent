package ru.mytumanov.starter.masking.service.strategy;

import ru.mytumanov.starter.masking.config.MaskingProperties;
import ru.mytumanov.starter.masking.model.MaskType;

/**
 * Стратегия маскирования email
 */
public class EmailMaskingStrategy implements MaskingStrategy {
    @Override
    public MaskType getType() {
        return MaskType.EMAIL;
    }

    @Override
    public String mask(String source, MaskingProperties.MaskRule properties) {
        if (source == null || source.isBlank()) {
            return source;
        }

        int atIndex = source.lastIndexOf('@');
        if (atIndex <= 0 || atIndex == source.length() - 1) {
            return source;
        }

        String name = source.substring(0, atIndex);
        String domain = source.substring(atIndex + 1);
        String maskChar = properties.getMaskChar();

        if (properties.isKeepLength()) {
            return maskKeepLength(name, maskChar) + "@" + domain;
        }

        return maskNotKeepLength(name, maskChar) + "@" + domain;
    }

    /**
     * Маскирование с фиксированной длиной маски 3 символа
     * 
     * @param source   часть email до @
     * @param maskChar символ маскирования
     * 
     * @return замаскированная часть email до @
     */
    private String maskNotKeepLength(String source, String maskChar) {
        int len = source.length();
        String maskString = maskChar.repeat(3);

        if (len >= 5) {
            return source.charAt(0) + maskString + source.charAt(len - 1);
        }

        return source.charAt(0) + maskString;
    }

    /**
     * Маскирование с сохранением длины
     * 
     * @param source   часть email до @
     * @param maskChar символ маскирования
     * 
     * @return замаскированная часть email до @
     */
    private String maskKeepLength(String source, String maskChar) {
        int len = source.length();

        if (len >= 3) {
            return source.charAt(0) + maskChar.repeat(len - 2) + source.charAt(len - 1);
        } else if (len > 1) {
            return source.charAt(0) + maskChar.repeat(len - 1);
        }

        return maskChar;
    }
}
