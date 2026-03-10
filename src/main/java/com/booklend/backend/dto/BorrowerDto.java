// Response DTO for Borrower information used in LendedBookDetailsDto as nested object.

package com.booklend.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowerDto {
    private int id;
    private String fullName;
    private String email;
    private String contactNo;
    private String whatsappNo;
    private String location;

}
