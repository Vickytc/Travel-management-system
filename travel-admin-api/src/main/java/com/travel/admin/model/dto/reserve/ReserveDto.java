package com.travel.admin.model.dto.reserve;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.admin.model.DtoConvertDo;
import com.travel.admin.model.domain.Reserve;
import com.travel.admin.model.valid.AddGroup;
import com.travel.admin.model.valid.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Data
public class ReserveDto implements DtoConvertDo<Reserve> {
    @NotNull(message = "Reservation ID cannot be empty when modifying", groups = UpdateGroup.class)
    private Integer id;

    /**
     * Hotel ID for the reservation
     */
    @NotNull(message="Hotel ID for the reservation cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private Integer hotelId;

    /**
     * Room ID for the reservation
     */
    @NotNull(message="Room ID for the reservation cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private Integer roomId;

    /**
     * Number of reservations
     */
    @NotNull(message="Reservation quantity cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private Integer count;

    /**
     * Check-in date
     */
    @NotNull(message="Check-in date cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date startDate;

    /**
     * Check-out date
     */
    @NotNull(message="Check-out date cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date endDate;

    /**
     * ID of the person making the reservation
     */
    @NotNull(message="User ID for the reservation cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private Integer userId;

    /**
     * Reservation status: 0 is CONFIRMED, 1 is ON HOLD, 2 is CANCELLED
     */
    @NotNull(message="Reservation status cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private Integer state;

    /**
     * Meal plans
     */
    @NotNull(message="Meal plans for the reserved room cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private List<String> mealPlanList;

    /**
     * Verification code
     */
    @NotBlank(message="Verification code cannot be empty")
    private String code;

    @Override
    public Reserve convert() {
        String mealPlans = JSONUtil.toJsonStr(mealPlanList);
        return Reserve.builder()
                .id(id)
                .hotelId(hotelId)
                .roomId(roomId)
                .count(count)
                .startDate(startDate)
                .endDate(endDate)
                .userId(userId)
                .state(state)
                .mealPlans(mealPlans)
                .build();
    }
}
