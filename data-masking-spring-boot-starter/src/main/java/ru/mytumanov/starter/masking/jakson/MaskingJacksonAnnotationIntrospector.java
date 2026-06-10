package ru.mytumanov.starter.masking.jakson;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import ru.mytumanov.starter.masking.annotation.Mask;
import ru.mytumanov.starter.masking.service.MaskingService;

/**
 * Интроспектор аннотаций Jackson для маскирования данных.
 * Находит аннотацию @Mask на свойствах и подключает MaskingJacksonSerializer.
 */
public class MaskingJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {

    private final MaskingService maskingService;

    public MaskingJacksonAnnotationIntrospector(MaskingService maskingService) {
        this.maskingService = maskingService;
    }

    @Override
    public Object findSerializer(Annotated a) {
        Mask mask = _findAnnotation(a, Mask.class);
        if (mask != null) {
            return new MaskingJacksonSerializer(maskingService, mask);
        }

        return super.findSerializer(a);
    }
}
