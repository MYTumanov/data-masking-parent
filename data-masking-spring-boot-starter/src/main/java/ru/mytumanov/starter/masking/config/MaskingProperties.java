package ru.mytumanov.starter.masking.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Свойства конфигурации для маскирования данных
 */
@ConfigurationProperties(prefix = "data-masking")
public class MaskingProperties {
    /**
     * Глобальное включение/выключение маскирования
     */
    private boolean enabled = true;

    /**
     * Правила маскирования для конкретного типа данных
     */
    private Map<String, MaskRule> rules = new HashMap<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, MaskRule> getRules() {
        return rules;
    }

    public void setRules(Map<String, MaskRule> rules) {
        this.rules = rules;
    }

    /**
     * Вложенный класс, описывающий правила маскирования для конкретного типа данных
     */
    public static class MaskRule {
        /**
         * Символ, используемый для маскирования
         */
        private String maskChar = "*";

        /**
         * Сохранять ли длину маскируемой строки
         */
        private boolean keepLength = false;

        public String getMaskChar() {
            return maskChar;
        }

        public void setMaskChar(String maskChar) {
            this.maskChar = maskChar;
        }

        public boolean isKeepLength() {
            return keepLength;
        }

        public void setKeepLength(boolean keepLength) {
            this.keepLength = keepLength;
        }
    }
}
