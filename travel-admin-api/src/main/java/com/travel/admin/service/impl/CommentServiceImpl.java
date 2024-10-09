package com.travel.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.admin.constant.StateConstant;
import com.travel.admin.exception.BusinessException;
import com.travel.admin.mapper.*;
import com.travel.admin.model.domain.*;
import com.travel.admin.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;
    private final RoomMapper roomMapper;
    private final HotelMapper hotelMapper;
    private final ReserveMapper reserveMapper;

    @Override
    public Page<Comment> getPageByCondition(Page<Comment> page, Comment comment) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        // User ID
        if (comment.getUserId() != null) {
            queryWrapper.lambda().eq(Comment::getUserId, comment.getUserId());
        }
        // Status
        if (comment.getState() != null) {
            queryWrapper.lambda().eq(Comment::getState, comment.getState());
        }
        // Hotel ID
        if (comment.getHotelId() != null) {
            queryWrapper.lambda().eq(Comment::getHotelId, comment.getHotelId());
        }
        Page<Comment> commentPage = commentMapper.selectPage(page, queryWrapper);
        fillDetail(commentPage.getRecords());
        return commentPage;
    }

    @Override
    public boolean addComment(Comment comment, Integer reserveId) {
        // Query commenter information
        User user = userMapper.selectById(comment.getUserId());
        if (user == null) {
            throw new BusinessException("Comment failed, commenter information is incomplete or does not exist");
        }
        comment.setUsername(user.getUsername());
        comment.setEmail(user.getEmail());
        comment.setImage(user.getImage());
        comment.setState(StateConstant.UNAUDITED);
        comment.setCreateTime(new Date());
        int row = commentMapper.insert(comment);
        // After commenting, update the comment flag in the reservation
        Reserve reserve = reserveMapper.selectById(reserveId);
        reserve.setCommentId(comment.getId());
        reserveMapper.updateById(reserve);
        return row > 0;
    }

    @Override
    public boolean deleteCommentById(Integer id) {
        return commentMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateComment(Comment comment) {
        return commentMapper.updateById(comment) > 0;
    }

    @Override
    public Page<Comment> getCommentAllByUserId(Page<Comment> page, Integer userId) {
        // Query all hotel lists of the user
        QueryWrapper<Hotel> hotelQueryWrapper = new QueryWrapper<>();
        hotelQueryWrapper.lambda().eq(Hotel::getUserId, userId);
        List<Hotel> hotelList = hotelMapper.selectList(hotelQueryWrapper);
        if (hotelList.size() == 0) {
            return null;
        }
        List<Integer> hotelIds = hotelList.stream().map(Hotel::getId).collect(Collectors.toList());
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.lambda().in(Comment::getHotelId, hotelIds);
        Page<Comment> commentPage = commentMapper.selectPage(page, commentQueryWrapper);
        fillDetail(commentPage.getRecords());
        return commentPage;
    }

    private void fillDetail(List<Comment> commentList) {
        if (commentList.size() == 0) {
            return;
        }
        List<Integer> roomIds = commentList.stream()
                .map(Comment::getRoomId)
                .collect(Collectors.toList());

        List<Integer> hotelIds = commentList.stream()
                .map(Comment::getHotelId)
                .distinct()
                .collect(Collectors.toList());

        // Query room information
        List<Room> rooms = roomMapper.selectBatchIds(roomIds);
        // Query hotel information
        List<Hotel> hotels = hotelMapper.selectBatchIds(hotelIds);

        // Create mappings for quick lookup
        Map<Integer, Room> roomMap = rooms.stream()
                .collect(Collectors.toMap(Room::getId, room -> room));

        Map<Integer, Hotel> hotelMap = hotels.stream()
                .collect(Collectors.toMap(Hotel::getId, hotel -> hotel));

        // Fill in reservation information
        for (Comment comment : commentList) {
            Room room = roomMap.get(comment.getRoomId());
            Hotel hotel = hotelMap.get(comment.getHotelId());

            if (room != null) {
                comment.setRoomName(room.getRoomName());
            }
            if (hotel != null) {
                comment.setHotelName(hotel.getHotelName());
            }
        }
    }
}





