package com.travel.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.admin.model.Result;
import com.travel.admin.model.domain.Room;
import com.travel.admin.model.dto.room.RoomDto;
import com.travel.admin.model.valid.AddGroup;
import com.travel.admin.model.valid.UpdateGroup;
import com.travel.admin.service.RoomService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Room-related API
 */
@RestController
@RequestMapping("/room")
public class RoomController {
    @Resource
    private RoomService roomService;

    /**
     * Paginated conditional query for the room list
     */
    @PostMapping("/page/{pageNum}/{pageSize}")
    public Result getPageByCondition(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                     @RequestBody Room room) {
        Page<Room> page = new Page<>(pageNum, pageSize);
        Page<Room> roomPage = roomService.getPageByCondition(page, room);
        return Result.success(roomPage);
    }

    /**
     * Add a room
     */
    @PostMapping
    public Result addRoom(@Validated(AddGroup.class) @RequestBody RoomDto dto) {
        Room room = dto.convert();
        return roomService.addRoom(room) ? Result.success("Room added successfully!") : Result.error("Failed to add room!");
    }

    /**
     * Delete a room
     */
    @DeleteMapping("/{id}")
    public Result deleteRoom(@Valid @PathVariable @NotNull(message="Delete room ID cannot be null") Integer id) {
        return roomService.deleteRoomById(id) ? Result.success("Room deleted successfully!") : Result.error("Failed to delete room!");
    }

    /**
     * Modify room information
     */
    @PutMapping
    public Result updateRoom(@Validated(UpdateGroup.class) @RequestBody RoomDto dto) {
        Room room = dto.convert();
        return roomService.updateRoom(room) ? Result.success("Room updated successfully!") : Result.error("Failed to update room!");
    }
}

