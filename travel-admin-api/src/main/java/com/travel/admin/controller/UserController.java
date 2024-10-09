package com.travel.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.travel.admin.exception.BusinessException;
import com.travel.admin.model.Result;
import com.travel.admin.model.domain.User;
import com.travel.admin.model.dto.user.*;
import com.travel.admin.service.UserService;
import com.travel.admin.model.valid.AddGroup;
import com.travel.admin.model.valid.UpdateGroup;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * User-related API
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * User registration
     */
    @PostMapping("/register")
    public Result userRegister(@RequestBody @Validated UserRegisterDto dto) {
        User user = dto.convert();
        return userService.userRegister(user, dto.getCode(), dto.getRecaptchaResponse())
                ? Result.success("Registration successful!") : Result.error("Registration failed!");
    }

    /**
     * User login
     */
    @PostMapping("/login")
    public Result userLogin(@RequestBody @Validated UserLoginDto dto) {
        String token = userService.userLogin(dto);
        return StrUtil.isNotBlank(token) ? Result.success("Login successful!", token) : Result.error("Login failed!");
    }

    /**
     * Query login information
     */
    @GetMapping("/loginInfo")
    public Result getLoginInfo() {
        User user = userService.getLoginInfo();
        return user != null ? Result.success(user) : Result.error("User not logged in!");
    }

    /**
     * Paginated conditional query for user list
     */
    @PostMapping("/page/{pageNum}/{pageSize}")
    public Result getPageByCondition(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                     @RequestBody User user) {
        Page<User> page = userService.getPageByCondition(new Page<User>(pageNum, pageSize), user);
        return Result.success(page);
    }

    /**
     * Add user
     */
    @PostMapping
    public Result addUser(@Validated(AddGroup.class) @RequestBody UserDto dto) {
        User user = dto.convert();
        return userService.addUser(user) ? Result.success("User added successfully!") : Result.error("Failed to add user!");
    }

    /**
     * Delete user
     */
    @DeleteMapping("/{id}")
    public Result deleteUser(@Valid @PathVariable @NotNull(message="User ID to delete cannot be empty") Integer id) {
        return userService.deleteUserById(id) ? Result.success("User deleted successfully!") : Result.error("Failed to delete user!");
    }

    /**
     * Update user
     */
    @PutMapping
    public Result updateUser(@Validated(UpdateGroup.class) @RequestBody UserDto dto) {
        User user = dto.convert();
        return userService.updateUser(user) ? Result.success("User updated successfully!") : Result.error("Failed to update user!");
    }

    /**
     * Query user list by different roles
     */
    @GetMapping("/list")
    public Result getListByRole(@RequestParam("role") Integer role, @RequestParam("state") Integer state) {
        List<User> userList = userService.getListByRoleAndState(role, state);
        return Result.success(userList);
    }

    /**
     * Send registration verification code
     */
    @GetMapping("/register/sendCode")
    public Result senCode(@RequestParam("email") String email) {
        if (StrUtil.isBlank(email)) {
            throw new BusinessException("Email parameter is empty");
        }
        return userService.sendCode(email) ? Result.success("Verification code sent successfully") : Result.error("Failed to send verification code");
    }
}

