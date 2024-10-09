package com.travel.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.admin.constant.StateConstant;
import com.travel.admin.exception.BusinessException;
import com.travel.admin.mapper.ReserveMapper;
import com.travel.admin.mapper.RoomMapper;
import com.travel.admin.mapper.UserMapper;
import com.travel.admin.model.domain.Hotel;
import com.travel.admin.model.domain.Reserve;
import com.travel.admin.model.domain.Room;
import com.travel.admin.model.domain.User;
import com.travel.admin.service.HotelService;
import com.travel.admin.mapper.HotelMapper;
import com.travel.admin.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel>
        implements HotelService {
    private final HotelMapper hotelMapper;
    private final UserMapper userMapper;
    private final ReserveMapper reserveMapper;
    private final RoomMapper roomMapper;

    @Override
    public Page<Hotel> getPageByCondition(Page<Hotel> page, Hotel hotel) {
        QueryWrapper<Hotel> queryWrapper = new QueryWrapper<>();
        // Hotel name
        if (StrUtil.isNotBlank(hotel.getHotelName())) {
            queryWrapper.lambda().like(Hotel::getHotelName, hotel.getHotelName());
        }
        // Hotel star rating
        if (hotel.getStarRating() != null) {
            queryWrapper.lambda().eq(Hotel::getStarRating, hotel.getStarRating());
        }
        // Hotel address
        if (StrUtil.isNotBlank(hotel.getHotelAddress())) {
            queryWrapper.lambda().like(Hotel::getHotelAddress, hotel.getHotelAddress());
        }
        // Hotel owner user ID
        if (hotel.getUserId() != null) {
            queryWrapper.lambda().eq(Hotel::getUserId, hotel.getUserId());
        }
        return hotelMapper.selectPage(page, queryWrapper);
    }

    @Override
    public boolean addHotel(Hotel hotel) {
        // Query the detailed information of the user who created the hotel
        User user = userMapper.selectById(hotel.getUserId());
        if (user == null) {
            throw new BusinessException("Addition failed, the hotel operator is not registered or information does not exist");
        }
        // Fill in detailed information into the hotel entity
        hotel.setBusinessRegistrationNumber(user.getBusinessRegistrationNumber());
        hotel.setContactUsername(user.getUsername());
        hotel.setContactPhoneNumber(user.getContactNumber());
        hotel.setContactEmail(user.getEmail());
        hotel.setState(StateConstant.NORMAL);
        hotel.setCreateTime(new Date());
        return hotelMapper.insert(hotel) > 0;
    }

    @Override
    public boolean deleteHotelById(Integer id) {
        // Check if there are customers with unfinished reservations at this hotel; if so, deletion fails
        QueryWrapper<Reserve> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Reserve::getHotelId, id)
                .eq(Reserve::getState, StateConstant.ON_HOLD);
        if (reserveMapper.selectCount(queryWrapper) != 0) {
            throw new BusinessException("Deletion failed, there are unfinished reservations at this hotel");
        }
        // Delete associated reservations
        queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Reserve::getHotelId, id);
        reserveMapper.delete(queryWrapper);
        // Delete associated rooms
        QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
        roomQueryWrapper.lambda().eq(Room::getHotelId, id);
        roomMapper.delete(roomQueryWrapper);
        return hotelMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateHotel(Hotel hotel) {
        return hotelMapper.updateById(hotel) > 0;
    }

    @Override
    public Hotel getHotelById(Integer id) {
        Hotel hotel = hotelMapper.selectById(id);
        if (hotel != null) {
            fillDetail(hotel);
        }
        return hotel;
    }

    @Override
    public List<Hotel> getList() {
        List<Hotel> hotelList = hotelMapper.selectList(new QueryWrapper<>());
        hotelList.forEach(this::fillDetail);
        return hotelList;
    }

    private void fillDetail(Hotel hotel) {
        if (hotel == null) {
            return;
        }
        // Query the name of the hotel operator
        User user = userMapper.selectById(hotel.getUserId());
        // Query the list of rooms in the hotel
        QueryWrapper<Room> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Room::getHotelId, hotel.getId());
        List<Room> roomList = roomMapper.selectList(queryWrapper);
        // Fill in information
        hotel.setUsername(user.getUsername());
        hotel.setRoomList(roomList);
        hotel.setRoomListJson(JSONUtil.toJsonStr(roomList));
    }
}





