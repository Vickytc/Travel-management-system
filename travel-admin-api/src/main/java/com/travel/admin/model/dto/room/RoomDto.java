package com.travel.admin.model.dto.room;

import cn.hutool.json.JSONUtil;
import com.travel.admin.model.DtoConvertDo;
import com.travel.admin.model.domain.Room;
import com.travel.admin.model.valid.AddGroup;
import com.travel.admin.model.valid.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Data
public class RoomDto implements DtoConvertDo<Room> {
    @NotNull(message = "Room ID cannot be empty when modifying", groups = UpdateGroup.class)
    private Integer id;

    /**
     * Room name
     */
    @NotBlank(message="Room name cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String roomName;

    /**
     * Room image
     */
    @NotBlank(message="Room image cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String image;

    /**
     * Room price
     */
    @NotNull(message = "Room price cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private BigDecimal price;

    /**
     * Room stock
     */
    @NotNull(message="Room stock cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private Integer stock;

    /**
     * ID of the hotel the room belongs to
     */
    @NotNull(message="Hotel ID for the room cannot be empty when adding", groups = {AddGroup.class})
    private Integer hotelId;

    /**
     * Room status: 0 is normal, 1 is being taken down
     */
    @NotNull(message="Room status cannot be empty when modifying", groups = {UpdateGroup.class})
    private Integer state;

    /**
     * Meal plans configuration map
     */
    @NotNull(message = "Meal plans configuration cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private Map<String, List<String>> mealPlansConfigMap;

    @Override
    public Room convert() {
        String mealPlansConfig = JSONUtil.toJsonStr(mealPlansConfigMap);
        return Room.builder()
                .id(id)
                .roomName(roomName)
                .image(image)
                .price(price)
                .mealPlansConfig(mealPlansConfig)
                .stock(stock)
                .hotelId(hotelId)
                .state(state)
                .build();
    }
}

