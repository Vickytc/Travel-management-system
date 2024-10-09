package com.travel.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.admin.constant.StateConstant;
import com.travel.admin.constant.TravelAdminProperties;
import com.travel.admin.constant.UserConstant;
import com.travel.admin.model.domain.User;
import com.travel.admin.model.dto.user.ForgotPasswordDto;
import com.travel.admin.model.dto.user.UserLoginDto;
import com.travel.admin.exception.BusinessException;
import com.travel.admin.mapper.*;
import com.travel.admin.service.EmailService;
import com.travel.admin.service.UserService;
import com.travel.admin.utils.JwtUtil;
import com.travel.admin.utils.Md5Util;
import com.travel.admin.utils.RegexUtil;
import com.travel.admin.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @description Database operation Service implementation for table [user]
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    private final UserMapper userMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final TravelAdminProperties properties;
    private final EmailService emailService;

    @Override
    public boolean userRegister(User user, String code, String recaptchaResponse) {
        // Check if the CAPTCHA verification is passed
        if (StrUtil.isBlank(recaptchaResponse)) {
            throw new BusinessException("CAPTCHA verification failed");
        }
//        boolean result = true;
//        try {
//            result = verifyRecaptcha(recaptchaResponse);
//        } catch (Exception e) {
//            log.error(e.toString());
//            throw new BusinessException("Request failed, CAPTCHA error");
//        }
//        if (!result) {
//            throw new BusinessException("Failed CAPTCHA verification, please register again");
//        }
        // Check if the verification code is correct
        String cacheCode = stringRedisTemplate.opsForValue().get(UserConstant.USER_REGISTER_CODE + user.getEmail());
        if (StrUtil.isBlank(cacheCode)) {
            throw new BusinessException("Verification code has expired");
        }
        if (!cacheCode.equals(code)) {
            throw new BusinessException("Verification code is incorrect");
        }
        user.setImage(UserConstant.DEFAULT_IMAGE);
        // Check if a user with the same name exists
        User temp = userMapper.getOneByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(temp)) {
            throw new BusinessException("A user with the same name exists: " + (user.getRole() == UserConstant.CUSTOM_ROLE ? "Customer" : "Hotel Merchant"));
        }
        user.setState(StateConstant.NORMAL);
        user.setCreateTime(new Date());
        // Encrypt the password
        user.setPassword(Md5Util.getMD5String(user.getPassword()));
        int row = userMapper.insert(user);
        // Send an email to the merchant user after successful registration
        if (user.getRole() == UserConstant.BUSINESS_ROLE) {
            String emailContent = String.format(
                    "Dear %s,\n\n" +
                            "Welcome to become our merchant! Our platform provides rich customer resources and powerful promotional tools to help you enhance your business!\n\n" +
                            "Your registration information is as follows:\n" +
                            "Name: %s\n" +
                            "Contact Number: %s\n" +
                            "Business Registration Number: %s\n\n" +
                            "Looking forward to cooperating with you!\n" +
                            "If you have any questions, please feel free to contact us.",
                    user.getUsername(), user.getUsername(), user.getContactNumber(), user.getBusinessRegistrationNumber()
            );
            emailService.sendSimpleEmail(user.getEmail(), "Welcome to join our platform!", emailContent);
        }
        return row > 0;
    }

    @Override
    public String userLogin(UserLoginDto dto) {
        User user = userMapper.getOneByUsername(dto.getUsername());
        // Check if the user exists
        if (user == null) {
            throw new BusinessException("Username does not exist");
        }
        // Check if the user is banned
        validateUserState(user);
        // Check user login error attempts
        checkLoginAttempts(user);
        // Check if the password is correct
        if (!Md5Util.checkPassword(dto.getPassword(), user.getPassword())) {
            incrementLoginErrorCount(user);
            throw new BusinessException("Login failed, password is incorrect");
        }
        return generateToken(user);
    }

    @Override
    public User getLoginInfo() {
        Integer userId = ThreadLocalUtil.get();
        return userMapper.selectById(userId);
    }

    @Override
    public boolean forgotPassword(ForgotPasswordDto dto) {
        // Query user by username
        User user = userMapper.getOneByUsername(dto.getUsername());
        // Check if the user exists
        if (user == null) {
            throw new BusinessException("Username does not exist");
        }
        // Check if the email matches
        if (!user.getEmail().equals(dto.getEmail())) {
            throw new BusinessException("Email does not match");
        }
        // If it matches, update the user's password
        user.setPassword(Md5Util.getMD5String(dto.getNewPassword()));
        return userMapper.updateById(user) > 0;
    }

    @Override
    public Page<User> getPageByCondition(Page<User> page, User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(user.getUsername())) {
            queryWrapper.lambda().eq(User::getUsername, user.getUsername());
        }
        if (user.getRole() != null) {
            queryWrapper.lambda().eq(User::getRole, user.getRole());
        }
        queryWrapper.lambda().orderByAsc(User::getCreateTime);
        return userMapper.selectPage(page, queryWrapper);
    }

    @Override
    public boolean addUser(User user) {
        User temp = userMapper.getOneByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(temp)) {
            throw new BusinessException("A user with the same name exists: " + (user.getRole() == UserConstant.CUSTOM_ROLE ? "Customer" : "Hotel Merchant"));
        }
        // Encrypt the password
        user.setPassword(Md5Util.getMD5String(user.getPassword()));
        // User status defaults to normal
        user.setState(StateConstant.NORMAL);
        user.setCreateTime(new Date());
        return userMapper.insert(user) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserById(Integer id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateUser(User user) {
        // Query original user information
        User oldUser = userMapper.selectById(user.getId());
        // Check if the password has changed, encrypt and assign if changed
        if (!oldUser.getPassword().equals(user.getPassword())) {
            user.setPassword(Md5Util.getMD5String(user.getPassword()));
        }
        // Update user data
        return userMapper.updateById(user) > 0;
    }

    @Override
    public List<User> getListByRoleAndState(Integer role, Integer state) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (role != null) {
            queryWrapper.lambda().eq(User::getRole, role);
        }
        if (state != null) {
            queryWrapper.lambda().eq(User::getState, state);
        }
        queryWrapper.lambda().orderByAsc(User::getCreateTime);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public boolean sendCode(String email) {
        // Validate email
        if(RegexUtil.isEmailInvalid(email)) {
            throw new BusinessException("Email format is incorrect");
        }
        // If successful, create a verification code
        String code = RandomUtil.randomNumbers(6);
        // Save verification code to Redis, set expiration, using email as key
        stringRedisTemplate.opsForValue().set(UserConstant.USER_REGISTER_CODE + email, code);
        // Send verification code
        emailService.sendVerificationCode(email, code);
        // Return result
        return true;
    }

    /**
     * Validate user state
     */
    private void validateUserState(User user) {
        if (user.getState() != null && user.getState() == 1) {
            throw new BusinessException("You have been banned");
        }
    }

    /**
     * Check login error attempts
     */
    private void checkLoginAttempts(User user) {
        String errorCountStr = stringRedisTemplate.opsForValue().get(UserConstant.USER_LOGIN_ERROR_COUNT_KEY + user.getId());
        if (StrUtil.isNotBlank(errorCountStr) && Integer.parseInt(errorCountStr) >= properties.getLogin().getErrorCount()) {
            throw new BusinessException("Login error attempts exceeded " + properties.getLogin().getErrorCount() +
                    " times, please try again after " + properties.getLogin().getLockTime() + " seconds");
        }
    }

    /**
     * Increment login error count
     */
    private void incrementLoginErrorCount(User user) {
        String errorCountKey = UserConstant.USER_LOGIN_ERROR_COUNT_KEY + user.getId();
        // Increase error count
        stringRedisTemplate.opsForValue().increment(errorCountKey);
        // Set timeout
        stringRedisTemplate.expire(errorCountKey, Duration.ofSeconds(properties.getLogin().getLockTime()));
    }

    /**
     * Generate JWT token
     */
    private String generateToken(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", user.getId());
        userMap.put("username", user.getUsername());

        String token = JwtUtil.genToken(userMap);
        // Store token
        stringRedisTemplate.opsForValue().set(UserConstant.USER_LOGIN_KEY + token, token, Duration.ofHours(24));

        return token;
    }

    /**
     * Verify CAPTCHA result
     */
    private boolean verifyRecaptcha(String recaptchaResponse) throws JsonProcessingException {
        String secretKey = properties.getRecaptcha().getSecretKey();
        String url = "https://www.google.com/recaptcha/api/siteverify";

        // Create request body
        String requestBody = String.format("secret=%s&response=%s", secretKey, recaptchaResponse);

        // Use RestTemplate to send POST request
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(url, requestBody, String.class);

        // Parse JSON response
        JsonNode jsonNode = new ObjectMapper().readTree(response);
        return jsonNode.get("success").asBoolean();
    }

}




