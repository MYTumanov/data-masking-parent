package ru.mytumanov.testapp;

import java.util.UUID;

import lombok.Data;
import ru.mytumanov.starter.masking.annotation.Mask;
import ru.mytumanov.starter.masking.model.MaskType;

@Data
public class UserDto {
    private UUID id;

    @Mask(type = MaskType.EMAIL)
    private String email;

    private String phoneNumber;

    private String lastName;

    private String firstName;

    private String middleName;
}
