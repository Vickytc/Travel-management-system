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
 * Comment Information Table
 * @TableName Comment
 */
@TableName(value ="Comment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    /**
     * Comment ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * Comment content
     */
    private String content;

    /**
     * Service rating
     */
    private Integer serviceRating;

    /**
     * Hotel ID
     */
    private Integer hotelId;

    /**
     * Room ID
     */
    private Integer roomId;

    /**
     * Commenter ID
     */
    private Integer userId;

    /**
     * Commenter name
     */
    private String username;

    /**
     * Commenter email
     */
    private String email;

    /**
     * Avatar
     */
    private String image;

    /**
     * Comment status, 0 means unreviewed, 1 means reviewed and approved
     */
    private Integer state;

    /**
     * Comment time
     */
    private Date createTime;

    /**
     * Hotel name
     */
    @TableField(exist = false)
    private String hotelName;

    /**
     * Room name
     */
    @TableField(exist = false)
    private String roomName;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
