package com.travel.admin.model.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * User login parameter
 */
@Data
public class UserLoginDto {
    /**
     * username
     */
    @NotBlank(message="Login username cannot be empty")
    private String username;

    /**
     * password
     */
    @NotBlank(message="Login password cannot be empty")
    private String password;

}
