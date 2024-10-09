package com.travel.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reservation Information Table
 * @TableName Reserve
 */
@TableName(value ="Reserve")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reserve implements Serializable {
    /**
     * Reservation ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * Hotel ID for the reservation
     */
    private Integer hotelId;

    /**
     * Room ID for the reservation
     */
    private Integer roomId;

    /**
     * Number of reservations
     */
    private Integer count;

    /**
     * Check-in date
     */
    private Date startDate;

    /**
     * Check-out date
     */
    private Date endDate;

    /**
     * Meal plans
     */
    private String mealPlans;

    /**
     * ID of the person making the reservation
     */
    private Integer userId;

    /**
     * Name of the person making the reservation
     */
    private String username;

    /**
     * Email of the person making the reservation
     */
    private String email;

    /**
     * Total amount
     */
    private BigDecimal totalAmount;

    /**
     * Comment ID
     */
    private Integer commentId;

    /**
     * Reservation status: 0 is CONFIRMED, 1 is ON HOLD, 2 is CANCELLED
     */
    private Integer state;

    /**
     * Creation time
     */
    private Date createTime;

    /**
     * List of meal plans
     */
    @TableField(exist = false)
    private List<String> mealPlanList;

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

    /**
     * Room price
     */
    @TableField(exist = false)
    private BigDecimal roomPrice;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
