package com.travel.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.admin.model.domain.Hotel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface HotelService extends IService<Hotel> {
    /**
     * Paginated conditional query for the hotel list
     */
    Page<Hotel> getPageByCondition(Page<Hotel> page, Hotel hotel);

    /**
     * Add a hotel
     */
    boolean addHotel(Hotel hotel);

    /**
     * Delete a hotel
     */
    boolean deleteHotelById(Integer id);

    /**
     * Update a hotel
     */
    boolean updateHotel(Hotel hotel);

    /**
     * Query hotel details
     */
    Hotel getHotelById(Integer id);

    /**
     * Query all hotel data collection
     */
    List<Hotel> getList();
}

