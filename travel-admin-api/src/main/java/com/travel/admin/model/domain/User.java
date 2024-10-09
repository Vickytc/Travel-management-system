package com.travel.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName User
 */
@TableName(value ="User")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    /**
     * Primary Key ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * Name
     */
    private String username;

    /**
     * Password
     */
    private String password;

    /**
     * Address
     */
    private String address;

    /**
     * Age
     */
    private Integer age;

    /**
     * Profile picture
     */
    private String image;

    /**
     * Business registration number
     */
    private String businessRegistrationNumber;

    /**
     * Contact number
     */
    private String contactNumber;

    /**
     * Email
     */
    private String email;

    /**
     * ID card
     */
    private String idCard;

    /**
     * Role: 0 is Admin, 1 is Hotel Operator, 2 is Customer
     */
    private Integer role;

    /**
     * Status: 0 is normal, 1 is banned
     */
    private Integer state;

    /**
     * Registration time
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
