package com.travel.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.admin.model.domain.Room;
import com.baomidou.mybatisplus.extension.service.IService;


public interface RoomService extends IService<Room> {
    /**
     * Paginated conditional query for the room list
     */
    Page<Room> getPageByCondition(Page<Room> page, Room room);

    /**
     * Add a room
     */
    boolean addRoom(Room room);

    /**
     * Delete a room
     */
    boolean deleteRoomById(Integer id);

    /**
     * Update room information
     */
    boolean updateRoom(Room room);
}

