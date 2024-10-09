package com.travel.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.travel.admin.model.Result;
import com.travel.admin.model.domain.User;
import com.travel.admin.model.dto.user.ForgotPasswordDto;
import com.travel.admin.model.dto.user.UserLoginDto;

import java.util.List;

/**
 * User service interface
 */
public interface UserService extends IService<User> {
    /**
     * User registration
     * @param user Registration parameters
     * @return Registration result
     */
    boolean userRegister(User user, String code, String recaptchaResponse);

    /**
     * User login
     * @param dto Login parameters
     * @return Login result
     */
    String userLogin(UserLoginDto dto);

    /**
     * Get login information
     * @return user
     */
    User getLoginInfo();

    /**
     * Forgot password, reset new password
     * @param dto Reset parameters
     * @return Reset result
     */
    boolean forgotPassword(ForgotPasswordDto dto);

    /**
     * Paginated conditional query for the user list
     * @param page Paginated data
     * @param user Query conditions
     * @return Paginated data
     */
    Page<User> getPageByCondition(Page<User> page, User user);

    /**
     * Add a user
     * @param user User entity
     * @return Addition result
     */
    boolean addUser(User user);

    /**
     * Delete a user
     * @param id User ID
     * @return Deletion result
     */
    boolean deleteUserById(Integer id);

    /**
     * Update user information
     * @param user User entity
     * @return Update result
     */
    boolean updateUser(User user);

    /**
     * Query user list by different roles
     * @param role Role
     * @param state Status
     * @return list
     */
    List<User> getListByRoleAndState(Integer role, Integer state);

    /**
     * Send verification code
     *
     * @param email the email
     * @return the result
     */
    boolean sendCode(String email);
}

