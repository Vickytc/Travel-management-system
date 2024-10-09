package com.travel.admin.listener;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.travel.admin.exception.BusinessException;
import com.travel.admin.model.domain.Hotel;
import com.travel.admin.model.domain.Room;
import com.travel.admin.service.HotelService;
import com.travel.admin.service.RoomService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Excel Import Listener
 */
@Slf4j
public class HotelExcelImportListener extends AnalysisEventListener<Hotel> {
    private final HotelService hotelService;
    private final RoomService roomService;

    private static final int BATCH_COUNT = 100;
    private List<Hotel> dataList = new ArrayList<>(BATCH_COUNT);

    public HotelExcelImportListener(HotelService hotelService, RoomService roomService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    @Override
    public void invoke(Hotel data, AnalysisContext context) {
        log.info("Parsed a hotel data entry: {}", data);
        // Add to the collection
        dataList.add(data);
        // Add when the maximum value is reached
        if (dataList.size() >= BATCH_COUNT) {
            saveHotelList();
            dataList = new ArrayList<>(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // Call save once more at the end to avoid any unsaved data in the collection
        saveHotelList();
        log.info("All hotel data parsing completed!");
    }

    private void saveHotelList() {
        int size = dataList.size();
        log.info("Total {} entries, starting to store in the database!", size);
        try {
            // Check if the data in the file already exists in the database to avoid duplication
            List<Integer> importHotelIds = dataList.stream().map(Hotel::getId).collect(Collectors.toList());
            if (importHotelIds.size() > 0) {
                List<Integer> hotelIds = hotelService.listByIds(importHotelIds).stream()
                        .map(Hotel::getId).collect(Collectors.toList());
                if (hotelIds.size() > 0) {
                    // Filter out duplicate data
                    dataList = dataList.stream().filter(item -> !hotelIds.contains(item.getId())).collect(Collectors.toList());
                    log.info("Found {} duplicate hotel data entries!", size - dataList.size());
                }
            }
            // Save data
            hotelService.saveBatch(dataList);
            // Iterate to save each hotel's room data
            for (Hotel data : dataList) {
                List<Room> roomList = JSONUtil.toList(data.getRoomListJson(), Room.class);
                // Filter out duplicate data
                List<Integer> importRoomIds = roomList.stream().map(Room::getId).collect(Collectors.toList());
                if (importHotelIds.size() > 0) {
                    List<Integer> roomIds = roomService.listByIds(importRoomIds).stream()
                            .map(Room::getId).collect(Collectors.toList());
                    if (roomIds.size() > 0) {
                        // Filter out duplicate data
                        roomList = roomList.stream().filter(item -> !roomIds.contains(item.getId())).collect(Collectors.toList());
                    }
                }
                roomService.saveBatch(roomList);
            }
        } catch (Exception e) {
            throw new BusinessException("Import failed, saving to database failed");
        }
        log.info("Total {} hotel data entries stored in the database successfully!", dataList.size());
    }
}

