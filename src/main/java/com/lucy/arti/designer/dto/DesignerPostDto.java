package com.lucy.arti.designer.dto;

import com.lucy.arti.config.Gender;
import lombok.Data;

@Data
public class DesignerPostDto {
    private String userName;
    private String phoneNumber;
    private Gender gender;
    private String introduce;
}
