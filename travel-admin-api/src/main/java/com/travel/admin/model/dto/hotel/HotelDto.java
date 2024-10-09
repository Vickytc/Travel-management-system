package com.travel.admin.model.dto.hotel;

import com.travel.admin.model.DtoConvertDo;
import com.travel.admin.model.domain.Hotel;
import com.travel.admin.model.valid.AddGroup;
import com.travel.admin.model.valid.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class HotelDto implements DtoConvertDo<Hotel> {
    @NotNull(message = "Hotel ID cannot be empty when modifying", groups = UpdateGroup.class)
    private Integer id;

    /**
     * Hotel name
     */
    @NotBlank(message="Hotel name cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String hotelName;

    /**
     * Hotel address
     */
    @NotBlank(message="Hotel address cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String hotelAddress;

    /**
     * Hotel image
     */
    @NotBlank(message="Hotel image cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String image;

    /**
     * Hotel description
     */
    @NotBlank(message="Hotel description cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String description;

    /**
     * Hotel star rating
     */
    @NotNull(message="Hotel star rating cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private Integer starRating;

    /**
     * Hotel official website URL
     */
    private String url;

    /**
     * ID of the hotel operator
     */
    @NotNull(message="Hotel owner user ID cannot be empty", groups = {AddGroup.class})
    private Integer userId;

    /**
     * Hotel status: 0 is operational, 1 is resting
     */
    @NotNull(message="Hotel status cannot be empty when modifying", groups = {UpdateGroup.class})
    private Integer state;

    @Override
    public Hotel convert() {
        return Hotel.builder()
                .id(id)
                .hotelName(hotelName)
                .hotelAddress(hotelAddress)
                .image(image)
                .description(description)
                .starRating(starRating)
                .url(url)
                .userId(userId)
                .state(state)
                .build();
    }
}

