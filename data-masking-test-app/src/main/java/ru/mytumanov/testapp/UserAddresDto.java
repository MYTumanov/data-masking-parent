package ru.mytumanov.testapp;

import lombok.Data;
import ru.mytumanov.starter.masking.annotation.Mask;
import ru.mytumanov.starter.masking.model.MaskType;

@Data
public class UserAddresDto {
    private String email;

    private String phoneNumber;
}
