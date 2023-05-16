package com.example.user.model;

import com.example.user.service.StringListConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class UserInfo {

    @Id
    @GeneratedValue
    private UUID uuid;

    private String name;

    private String firstName;
    private String lastName;
    private String gender;
    private int age;

    @Convert(converter = StringListConverter.class)
    private List<String> interests;

    private String locality;
    private int growth;
    private int interestedGrowth;
    private String familyView;
    private String religion;
    private String religionImportance;
    private String searchTarget;

    @Convert(converter = StringListConverter.class)
    private List<String> characterAccentuations;

    @Convert(converter = StringListConverter.class)
    private List<String> interestedCharacterAccentuations;

    private String avatar;
}
