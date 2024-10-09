package com.travel.admin.interceptor;

import com.travel.admin.constant.UserConstant;
import com.travel.admin.utils.JwtUtil;
import com.travel.admin.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Login Interceptor
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Get the token from the request header
        String token = request.getHeader("Authorization");
        try {
            // Compare the token stored in Redis with the token in the request header
            String redisToken = stringRedisTemplate.opsForValue().get(UserConstant.USER_LOGIN_KEY + token);
            if (redisToken == null) {
                throw new RuntimeException("Token has expired");
            }
            Map<String, Object> map = JwtUtil.parseToken(token);
            Integer userId = (Integer) map.get("userId");
            // Store userId in local ThreadLocal
            ThreadLocalUtil.set(userId);
            // Allow the request to proceed
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}

