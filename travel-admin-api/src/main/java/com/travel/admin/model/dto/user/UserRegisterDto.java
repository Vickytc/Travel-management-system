package com.travel.admin.model.dto.user;

import com.travel.admin.model.DtoConvertDo;
import com.travel.admin.model.domain.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * User registration input parameters
 */
@Data
public class UserRegisterDto implements DtoConvertDo<User> {
    /**
     * Username
     */
    @NotBlank(message="Username cannot be empty during registration")
    private String username;

    /**
     * Password
     */
    @NotBlank(message="Password cannot be empty during registration")
    private String password;

    /**
     * Age
     */
    @NotNull(message="Age cannot be empty during registration")
    private Integer age;

    /**
     * Email
     */
    @NotBlank(message="Email cannot be empty during registration")
    private String email;

    /**
     * Contact number
     */
    @NotBlank(message="Contact number cannot be empty during registration")
    private String contactNumber;

    /**
     * Address
     */
    @NotBlank(message="Address cannot be empty during registration")
    private String address;

    /**
     * Business registration number
     */
    @NotBlank(message="Business registration number cannot be empty during registration")
    private String businessRegistrationNumber;

    /**
     * ID card
     */
    @NotBlank(message="ID card cannot be empty during registration")
    private String idCard;

    /**
     * User role: 0 is Admin, 1 is Hotel Operator, 2 is Customer
     */
    @NotNull(message="User role cannot be empty during registration")
    private Integer role;

    /**
     * Verification code
     */
    @NotBlank(message="Verification code cannot be empty during registration")
    private String code;

    /**
     * CAPTCHA result
     */
    private String recaptchaResponse;

    @Override
    public User convert() {
        return User.builder()
                .username(username)
                .password(password)
                .age(age)
                .email(email)
                .contactNumber(contactNumber)
                .address(address)
                .businessRegistrationNumber(businessRegistrationNumber)
                .idCard(idCard)
                .role(role)
                .build();
    }
}

