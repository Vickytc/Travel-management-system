package com.travel.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Room Information Table
 * @TableName Room
 */
@TableName(value ="Room")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room implements Serializable {
    /**
     * Room ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * Room name
     */
    private String roomName;

    /**
     * Room image
     */
    private String image;

    /**
     * Room price
     */
    private BigDecimal price;

    /**
     * Room meal plans configuration
     */
    private String mealPlansConfig;

    /**
     * Room stock
     */
    private Integer stock;

    /**
     * Hotel ID the room belongs to
     */
    private Integer hotelId;

    /**
     * Room status: 0 is normal, 1 is being taken down
     */
    private Integer state;

    /**
     * Creation time
     */
    private Date createTime;

    /**
     * Meal plans configuration map
     */
    @TableField(exist = false)
    private Map<String, List<String>> mealPlansConfigMap;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
