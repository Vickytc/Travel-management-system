package com.travel.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.admin.model.domain.Reserve;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ReserveService extends IService<Reserve> {
    /**
     * Paginated conditional query for the reservation list
     */
    Page<Reserve> getPageByCondition(Page<Reserve> page, Reserve reserve);

    /**
     * Reserve a room
     */
    boolean addReserve(Reserve reserve, String code);

    /**
     * Delete reservation information
     */
    boolean deleteReserveById(Integer id);

    /**
     * Update reservation information
     */
    boolean updateReserve(Reserve reserve);

    Page<Reserve> getReserveAllByUserId(Page<Reserve> page, Integer userId);
}

