package ru.mytumanov.starter.masking.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.mytumanov.starter.masking.model.MaskType;

/**
 * Аннотация для маскирования данных
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Mask {
    MaskType type();

    String pattern() default "";

    String replacement() default "*";
}
