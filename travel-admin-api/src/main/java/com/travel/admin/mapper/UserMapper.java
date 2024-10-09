package com.travel.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.admin.model.domain.User;
import org.apache.ibatis.annotations.Param;

/**
* @description Database operation Mapper for table [user]
* @Entity cn.edu.onlineteaching.model.User
*/
public interface UserMapper extends BaseMapper<User> {
    /**
     * Query users by username
     * @param username username
     * @return user
     */
    User getOneByUsername(@Param("username") String username);
}




