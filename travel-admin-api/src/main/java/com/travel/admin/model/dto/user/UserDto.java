package com.travel.admin.model.dto.user;

import com.travel.admin.model.DtoConvertDo;
import com.travel.admin.model.domain.User;
import com.travel.admin.model.valid.AddGroup;
import com.travel.admin.model.valid.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * User add/update input parameters
 */
@Data
public class UserDto implements DtoConvertDo<User> {
    /**
     * User ID
     */
    @NotNull(message = "User ID cannot be empty when modifying", groups = UpdateGroup.class)
    private Integer id;

    /**
     * Name
     */
    @NotBlank(message="Username cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String username;

    /**
     * Password
     */
    @NotBlank(message="Password cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String password;

    /**
     * Address
     */
    @NotBlank(message="Address cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String address;

    /**
     * Age
     */
    @NotNull(message="User age cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private Integer age;

    /**
     * Profile picture
     */
    @NotBlank(message="Profile picture cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String image;

    /**
     * Business registration number
     */
    @NotBlank(message="Business registration number cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String businessRegistrationNumber;

    /**
     * Contact number
     */
    @NotBlank(message="Contact number cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String contactNumber;

    /**
     * Email
     */
    @NotBlank(message="Email cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String email;

    /**
     * ID card
     */
    @NotBlank(message="ID card cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String idCard;

    /**
     * Role: 0 is Admin, 1 is Hotel Operator, 2 is Customer
     */
    @NotNull(message="User role cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private Integer role;

    /**
     * Status: 0 is normal, 1 is banned
     */
    @NotNull(message="User status cannot be empty", groups = {UpdateGroup.class})
    private Integer state;

    @Override
    public User convert() {
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .age(age)
                .email(email)
                .contactNumber(contactNumber)
                .address(address)
                .businessRegistrationNumber(businessRegistrationNumber)
                .idCard(idCard)
                .image(image)
                .role(role)
                .state(state)
                .build();
    }
}

