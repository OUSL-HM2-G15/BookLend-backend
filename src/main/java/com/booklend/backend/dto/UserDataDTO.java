package com.booklend.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDataDTO {
    private String username;
    private String role; // frontend may need to know user role
    private String fullName;
    private String email;
    private String contactNumber;
    private String whatsappNumber;
    private String profilePic;  
    private String locationName; // Frontend only need location name. Id is not necessary
}