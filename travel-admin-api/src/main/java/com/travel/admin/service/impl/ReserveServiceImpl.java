package com.travel.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.admin.constant.StateConstant;
import com.travel.admin.constant.UserConstant;
import com.travel.admin.exception.BusinessException;
import com.travel.admin.mapper.HotelMapper;
import com.travel.admin.mapper.RoomMapper;
import com.travel.admin.mapper.UserMapper;
import com.travel.admin.model.domain.*;
import com.travel.admin.service.EmailService;
import com.travel.admin.service.ReserveService;
import com.travel.admin.mapper.ReserveMapper;
import com.travel.admin.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReserveServiceImpl extends ServiceImpl<ReserveMapper, Reserve>
        implements ReserveService {
    private final ReserveMapper reserveMapper;
    private final UserMapper userMapper;
    private final RoomMapper roomMapper;
    private final HotelMapper hotelMapper;
    private final EmailService emailService;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public Page<Reserve> getPageByCondition(Page<Reserve> page, Reserve reserve) {
        QueryWrapper<Reserve> queryWrapper = new QueryWrapper<>();
        // Reservation ID
        if (reserve.getId() != null) {
            queryWrapper.lambda().eq(Reserve::getId, reserve.getId());
        }
        // Customer name
        if (StrUtil.isNotBlank(reserve.getUsername())) {
            queryWrapper.lambda().like(Reserve::getUsername, reserve.getUsername());
        }
        // Email
        if (StrUtil.isNotBlank(reserve.getEmail())) {
            queryWrapper.lambda().eq(Reserve::getEmail, reserve.getEmail());
        }
        // Reservation status
        if (reserve.getState() != null) {
            queryWrapper.lambda().eq(Reserve::getState, reserve.getState());
        }
        // Reserving user ID
        if (reserve.getUserId() != null) {
            queryWrapper.lambda().eq(Reserve::getUserId, reserve.getUserId());
        }
        Page<Reserve> reservePage = reserveMapper.selectPage(page, queryWrapper);
        fillDetail(reservePage.getRecords());
        return reservePage;
    }

    @Override
    public boolean addReserve(Reserve reserve, String code) {
        // Query the reserving person's information
        User user = userMapper.selectById(reserve.getUserId());
        if (user == null) {
            throw new BusinessException("Reservation failed, reserving person's information is incomplete or does not exist");
        }
        // Check if the verification code is correct
        String cacheCode = stringRedisTemplate.opsForValue().get(UserConstant.USER_REGISTER_CODE + user.getEmail());
        if (StrUtil.isBlank(cacheCode)) {
            throw new BusinessException("Verification code has expired");
        }
        if (!cacheCode.equals(code)) {
            throw new BusinessException("Verification code is incorrect");
        }
        // Query room price
        Room room = roomMapper.selectById(reserve.getRoomId());
        if (room == null) {
            throw new BusinessException("Reservation failed, room information is incomplete or does not exist");
        }
        // Calculate the total reservation amount
        BigDecimal totalAmount = calculateTotalAmount(room.getPrice(), reserve.getCount());
        // Fill in information
        reserve.setTotalAmount(totalAmount);
        reserve.setUsername(user.getUsername());
        reserve.setEmail(user.getEmail());
        reserve.setCreateTime(new Date());
        // Deduct the corresponding room stock
        if (room.getStock() - reserve.getCount() <= 0) {
            throw new BusinessException("Reservation failed, room stock is insufficient");
        }
        room.setStock(room.getStock() - reserve.getCount());
        roomMapper.updateById(room);
        int row = reserveMapper.insert(reserve);

        // Send reservation confirmation email
        sendReservationConfirmationEmail(user.getEmail(), reserve);
        return row > 0;
    }

    @Override
    public boolean deleteReserveById(Integer id) {
        return reserveMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateReserve(Reserve reserve) {
        // Query original reservation information to check if the reservation status has changed
        Reserve temp = reserveMapper.selectById(reserve.getId());
        boolean stateChanged = !temp.getState().equals(reserve.getState());

        if (stateChanged) {
            // Determine what the modified status is; if it is cancelled, return the room stock
            if (reserve.getState() == StateConstant.CANCELLED) {
                Room room = roomMapper.selectById(temp.getRoomId());
                if (room != null) {
                    // Return stock
                    room.setStock(room.getStock() + temp.getCount());
                    roomMapper.updateById(room);
                }
            }
            // Send status change email
            fillDetail(Collections.singletonList(temp));
            sendReservationStatusChangeEmail(temp, reserve.getState());
        }

        return reserveMapper.updateById(reserve) > 0;
    }

    @Override
    public Page<Reserve> getReserveAllByUserId(Page<Reserve> page, Integer userId) {
        // Query all hotel lists of the user
        QueryWrapper<Hotel> hotelQueryWrapper = new QueryWrapper<>();
        hotelQueryWrapper.lambda().eq(Hotel::getUserId, userId);
        List<Hotel> hotelList = hotelMapper.selectList(hotelQueryWrapper);
        if (hotelList.size() == 0) {
            return null;
        }
        List<Integer> hotelIds = hotelList.stream().map(Hotel::getId).collect(Collectors.toList());
        QueryWrapper<Reserve> reserveQueryWrapper = new QueryWrapper<>();
        reserveQueryWrapper.lambda().in(Reserve::getHotelId, hotelIds);
        Page<Reserve> reservePage = reserveMapper.selectPage(page, reserveQueryWrapper);
        fillDetail(reservePage.getRecords());
        return reservePage;
    }

    private void fillDetail(List<Reserve> reserveList) {
        if (reserveList.size() == 0) {
            return;
        }
        // Get collections of all room IDs and hotel IDs
        List<Integer> roomIds = reserveList.stream()
                .map(Reserve::getRoomId)
                .collect(Collectors.toList());

        List<Integer> hotelIds = reserveList.stream()
                .map(Reserve::getHotelId)
                .distinct()
                .collect(Collectors.toList());

        // Query room information
        List<Room> rooms = roomMapper.selectBatchIds(roomIds);
        // Query hotel information
        List<Hotel> hotels = hotelMapper.selectBatchIds(hotelIds);

        // Create maps for quick lookup
        Map<Integer, Room> roomMap = rooms.stream()
                .collect(Collectors.toMap(Room::getId, room -> room));

        Map<Integer, Hotel> hotelMap = hotels.stream()
                .collect(Collectors.toMap(Hotel::getId, hotel -> hotel));

        // Fill in reservation information
        for (Reserve reserve : reserveList) {
            Room room = roomMap.get(reserve.getRoomId());
            Hotel hotel = hotelMap.get(reserve.getHotelId());

            if (room != null) {
                reserve.setRoomName(room.getRoomName());
                reserve.setRoomPrice(room.getPrice());
            }
            if (hotel != null) {
                reserve.setHotelName(hotel.getHotelName());
            }

            reserve.setMealPlanList(JSONUtil.toList(reserve.getMealPlans(), String.class));
        }
    }

    /**
     * Calculate total amount
     * @param roomPrice Room price
     * @param count Reservation quantity
     * @return Calculated total amount
     */
    private BigDecimal calculateTotalAmount(BigDecimal roomPrice, Integer count) {
        return roomPrice.multiply(new BigDecimal(count))
                // Add 10% tax
                .multiply(new BigDecimal("1.10"));
    }

    /**
     * Send reservation success email
     */
    private void sendReservationConfirmationEmail(String toEmail, Reserve reserve) {
        fillDetail(Collections.singletonList(reserve));
        String statusMessage = (reserve.getState() == StateConstant.CANCELLED) ? "Cancelled" : "Confirmed";
        String subject = "[TravelAdmin] Reservation Confirmation";
        String text = String.format(
                "Dear Customer: %s\n\nYour reservation has been successfully made, details are as follows:\n" +
                        "Reservation ID: %s\n" +
                        "Hotel Name: %s\n" +
                        "Room Name: %s\n" +
                        "Reservation Quantity: %s\n" +
                        "Total Amount: %.2f\n" +
                        "Reservation Status: %s\n\n" +
                        "Thank you for your reservation!\n(Please do not reply to this email)",
                reserve.getUsername(), reserve.getId(), reserve.getHotelName(),
                reserve.getRoomName(), reserve.getCount(), reserve.getTotalAmount(), statusMessage
        );
        // Send email
        emailService.sendSimpleEmail(toEmail, subject, text);
    }

    /**
     * Send reservation status change email
     */
    private void sendReservationStatusChangeEmail(Reserve reserve, Integer newState) {
        String subject = "[TravelAdmin] Reservation Status Change Notification";
        String statusMessage = (newState == StateConstant.CANCELLED) ? "Cancelled" : "Confirmed";

        String text = String.format(
                "Dear Customer: %s\n\nThe reservation status for ID [%s] has changed to: %s.\n\n" +
                        "Details are as follows:\n" +
                        "Reservation ID: %s\n" +
                        "Hotel Name: %s\n" +
                        "Room Name: %s\n" +
                        "Reservation Quantity: %s\n" +
                        "Total Amount: %.2f\n" +
                        "Reservation Status: %s\n\n" +
                        "If you have any questions, please contact customer service.\n" +
                        "(Please do not reply to this email)",
                reserve.getUsername(), reserve.getId(), statusMessage,
                reserve.getId(), reserve.getHotelName(),
                reserve.getRoomName(), reserve.getCount(), reserve.getTotalAmount(), statusMessage
        );
        // Send email
        emailService.sendSimpleEmail(reserve.getEmail(), subject, text);
    }
}




