package com.travel.admin.model.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Forgot password request
 */
@Data
public class ForgotPasswordDto {
    /**
     * 用户名
     */
    @NotBlank(message="Username cannot be empty")
    private String username;

    /**
     * 新密码
     */
    @NotBlank(message="New password cannot be empty")
    private String newPassword;

    /**
     * 邮箱
     */
    @NotBlank(message="Email address cannot be empty")
    private String email;
}
