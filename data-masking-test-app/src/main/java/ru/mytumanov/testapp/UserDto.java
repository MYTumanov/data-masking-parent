package ru.mytumanov.testapp;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;
import ru.mytumanov.starter.masking.annotation.Mask;
import ru.mytumanov.starter.masking.jakson.MaskingJacksonSerializer;
import ru.mytumanov.starter.masking.model.MaskType;

@Data
public class UserDto {
    private UUID id;

    @Mask(type = MaskType.EMAIL)                                                                                                                                      
    @JsonSerialize(using = MaskingJacksonSerializer.class)    
    private String email;

    @Mask(type = MaskType.PHONE)
    private String phoneNumber;

    @Mask(type = MaskType.PHONE)
    private Long phoneNumberLong;

    private String lastName;

    private String firstName;

    @JsonIgnore
    private String middleName;

    private List<UserAddresDto> address;
}
