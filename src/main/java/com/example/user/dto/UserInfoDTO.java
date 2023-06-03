package com.example.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserInfoDTO {
    private UUID uuid;

    private String name;

    private String firstName;
    private String lastName;
    private String gender;
    private int age;
    private List<String> interests;
    private String locality;
    private String familyView;
    private String religion;
    private String religionImportance;
    private String searchTarget;
    private String aboutMe;
    private String attitudeToSmoking;
    private String attitudeToAlcohol;
    private List<String> characterAccentuations;
    private List<String> interestedCharacterAccentuations;
    private List<String> interestedPersonalityQualities;
    private String avatar;
}
