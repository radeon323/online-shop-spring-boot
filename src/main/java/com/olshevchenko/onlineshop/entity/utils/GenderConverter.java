package com.olshevchenko.onlineshop.entity.utils;

import com.olshevchenko.onlineshop.entity.Gender;

import javax.persistence.AttributeConverter;

/**
 * @author Oleksandr Shevchenko
 */
public class GenderConverter implements AttributeConverter<Gender, String> {
    @Override
    public String convertToDatabaseColumn(Gender gender) {
        String genderName = Gender.MALE.name();
        if (gender.equals(Gender.FEMALE)) {
            genderName = Gender.FEMALE.name();
        }
        return genderName;
    }

    @Override
    public Gender convertToEntityAttribute(String genderName) {
        Gender gender = Gender.MALE;
        if (Gender.FEMALE.name().equals(genderName)) {
            gender = Gender.FEMALE;
        }
        return gender;
    }
}
