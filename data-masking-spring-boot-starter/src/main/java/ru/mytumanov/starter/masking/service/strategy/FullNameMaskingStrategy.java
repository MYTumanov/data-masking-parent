package ru.mytumanov.starter.masking.service.strategy;

import java.util.regex.Pattern;

import ru.mytumanov.starter.masking.config.MaskingProperties.MaskRule;
import ru.mytumanov.starter.masking.model.MaskType;

/**
 * Стратегия маскирования ФИО (Full Name)
 */
public class FullNameMaskingStrategy implements MaskingStrategy {
    private static final int DEFAULT_MASK_LENGTH = 3;
    private static final Pattern WORD_PATTERN = Pattern.compile("\\S+");

    @Override
    public MaskType getType() {
        return MaskType.FULL_NAME;
    }

    @Override
    public String mask(String source, MaskRule properties) {
        if (source == null || source.isBlank()) {
            return source;
        }
        String maskChar = properties.getMaskChar();
        boolean keepLength = properties.isKeepLength();

        return WORD_PATTERN.matcher(source)
                .replaceAll(matchResult -> maskWord(matchResult.group(), maskChar, keepLength));
    }

    private String maskWord(String word, String maskChar, boolean keepLength) {
        int len = word.length();

        if (len >= 3) {
            int repeatCount = keepLength ? (len - 2) : DEFAULT_MASK_LENGTH;

            return word.charAt(0) + maskChar.repeat(repeatCount) + word.charAt(len - 1);
        } else if (len == 2) {
            int repeatCount = keepLength ? (len - 1) : DEFAULT_MASK_LENGTH;

            return word.charAt(0) + maskChar.repeat(repeatCount);
        } else if (len == 1) {
            String result = keepLength ? maskChar : word + maskChar.repeat(DEFAULT_MASK_LENGTH);

            return result;
        }

        return word;
    }
}
