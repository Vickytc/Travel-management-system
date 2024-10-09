package com.travel.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.admin.constant.StateConstant;
import com.travel.admin.exception.BusinessException;
import com.travel.admin.mapper.ReserveMapper;
import com.travel.admin.model.domain.Reserve;
import com.travel.admin.model.domain.Room;
import com.travel.admin.service.RoomService;
import com.travel.admin.mapper.RoomMapper;
import com.travel.admin.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room>
        implements RoomService{
    private final RoomMapper roomMapper;
    private final ReserveMapper reserveMapper;

    @Override
    public Page<Room> getPageByCondition(Page<Room> page, Room room) {
        QueryWrapper<Room> queryWrapper = new QueryWrapper<>();
        // Room Name
        if (StrUtil.isNotBlank(room.getRoomName())) {
            queryWrapper.lambda().like(Room::getRoomName, room.getRoomName());
        }
        // Hotel ID
        if (room.getHotelId() != null) {
            queryWrapper.lambda().eq(Room::getHotelId, room.getHotelId());
        }
        // Room Status
        if (room.getState() != null) {
            queryWrapper.lambda().eq(Room::getState, room.getState());
        }
        Page<Room> roomPage = roomMapper.selectPage(page, queryWrapper);
        fillDetail(roomPage.getRecords());
        return roomPage;
    }

    @Override
    public boolean addRoom(Room room) {
        // Check if room stock is greater than 0
        if (room.getStock() <= 0) {
            throw new BusinessException("Failed to add room, stock cannot be less than or equal to 0");
        }
        room.setState(StateConstant.NORMAL);
        room.setCreateTime(new Date());
        return roomMapper.insert(room) > 0;
    }

    @Override
    public boolean deleteRoomById(Integer id) {
        // Check if there are any unfinished reservations for this room
        QueryWrapper<Reserve> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Reserve::getRoomId, id)
                .eq(Reserve::getState, StateConstant.ON_HOLD);
        if (reserveMapper.selectCount(queryWrapper) != 0) {
            throw new BusinessException("Failed to delete room, there are unfinished reservations for this room");
        }
        queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Reserve::getRoomId, id);
        reserveMapper.delete(queryWrapper);
        return roomMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateRoom(Room room) {
        return roomMapper.updateById(room) > 0;
    }

    private void fillDetail(List<Room> roomList) {
        if (roomList.size() == 0) {
            return;
        }
        for (Room room : roomList) {
            String jsonString = room.getMealPlansConfig();
            if (StrUtil.isBlank(jsonString)) {
                continue;
            }
            // Use JSONUtil to convert string to JSONObject
            JSONObject jsonObject = JSONUtil.parseObj(jsonString);
            Map<String, List<String>> map = new HashMap<>();

            // Manually parse JSONObject into Map<String, List<String>>
            for (String key : jsonObject.keySet()) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                List<String> list = jsonArray.toList(String.class);
                map.put(key, list);
            }
            room.setMealPlansConfigMap(map);
        }
    }
}





