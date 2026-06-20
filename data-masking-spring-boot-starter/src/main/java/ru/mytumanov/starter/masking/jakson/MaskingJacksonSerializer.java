package ru.mytumanov.starter.masking.jakson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import ru.mytumanov.starter.masking.annotation.Mask;
import ru.mytumanov.starter.masking.model.MaskType;
import ru.mytumanov.starter.masking.service.MaskingService;

public class MaskingJacksonSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private final MaskingService maskingService;
    private final Mask maskAnnotation;

    // Конструктор для базовой регистрации в модуле
    public MaskingJacksonSerializer() {
        this.maskingService = null;
        this.maskAnnotation = null;
    }

    // Конструктор для создания контекстного экземпляра с метаданными аннотации
    public MaskingJacksonSerializer(MaskingService maskingService, Mask maskAnnotation) {
        this.maskingService = maskingService;
        this.maskAnnotation = maskAnnotation;
    }

    /**
     * Создает контекстный сериализатор на основе информации о сериализуемом
     * свойстве.
     *
     * @param provider провайдер сериализаторов, используемый для поиска стандартных
     *                 сериализаторов.
     * @param property метаданные свойства, которое подлежит сериализации.
     * 
     * @return сериализатор для обработки значения свойства.
     * @throws JsonMappingException в случае ошибок при поиске сериализатора.
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
            throws JsonMappingException {
        if (property == null) {
            return this;
        }

        if (property.getType().getRawClass() != String.class) {
            return provider.findValueSerializer(property.getType(), property);
        }

        Mask maskAnnotation = property.getAnnotation(Mask.class);
        if (maskAnnotation == null) {
            return provider.findValueSerializer(property.getType(), property);
        }

        return new MaskingJacksonSerializer(this.maskingService, maskAnnotation);
    }

    /**
     * Сериализует строковое значение, применяя к нему маскирование перед записью в
     * JSON.
     * <p>
     * Метод извлекает тип маскирования из аннотации {@link Mask},
     * маскирует переданное строковое значение с помощью {@link MaskingService}
     * и записывает результат как строковое поле JSON.
     * </p>
     *
     * @param value       исходное строковое значение для маскирования и
     *                    сериализации.
     * @param gen         генератор JSON для вывода сериализованных данных.
     * @param serializers провайдер сериализаторов.
     * @throws IOException в случае возникновения ошибок ввода-вывода при записи
     *                     JSON.
     */
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        MaskType maskType = this.maskAnnotation.type();
        String maskedValue = maskingService.mask(value, maskType);
        gen.writeString(maskedValue);
    }
}
