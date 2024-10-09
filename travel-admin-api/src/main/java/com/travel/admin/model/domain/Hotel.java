package com.travel.admin.model.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Hotel Information Table
 * @TableName Hotel
 */
@TableName(value ="Hotel")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel implements Serializable {
    /**
     * Hotel ID
     */
    @TableId(type = IdType.AUTO)
    @ExcelProperty("ID")
    private Integer id;

    /**
     * Hotel name
     */
    @ExcelProperty("Hotel Name")
    private String hotelName;

    /**
     * Hotel address
     */
    @ExcelProperty("Hotel Address")
    private String hotelAddress;

    /**
     * Hotel image
     */
    @ExcelProperty("Hotel Image")
    private String image;

    /**
     * Hotel description
     */
    @ExcelProperty("Hotel Description")
    private String description;

    /**
     * Hotel star rating
     */
    @ExcelProperty("Hotel Star Rating")
    private Integer starRating;

    /**
     * Hotel official website URL
     */
    @ExcelProperty("Hotel Official Website URL")
    private String url;

    /**
     * Hotel business registration number
     */
    @ExcelProperty("Hotel Business Registration Number")
    private String businessRegistrationNumber;

    /**
     * ID of the hotel operator
     */
    @ExcelProperty("Hotel Operator")
    private Integer userId;

    /**
     * Hotel contact person's name
     */
    @ExcelProperty("Hotel Contact Person Name")
    private String contactUsername;

    /**
     * Hotel contact person's email
     */
    @ExcelProperty("Hotel Contact Person Email")
    private String contactEmail;

    /**
     * Hotel contact person's phone number
     */
    @ExcelProperty("Hotel Contact Person Phone")
    private String contactPhoneNumber;

    /**
     * Hotel status, 0 means open, 1 means closed
     */
    @ExcelProperty("Hotel Status")
    private Integer state;

    /**
     * Creation time
     */
    @ExcelProperty("Creation Time")
    private Date createTime;

    /**
     * Hotel operator's name
     */
    @TableField(exist = false)
    @ExcelIgnore
    private String username;

    /**
     * List of rooms in the hotel
     */
    @TableField(exist = false)
    @ExcelIgnore
    private List<Room> roomList;

    @ExcelProperty("Room List")
    @TableField(exist = false)
    private String roomListJson;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
