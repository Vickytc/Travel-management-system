package com.travel.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.admin.listener.HotelExcelImportListener;
import com.travel.admin.model.Result;
import com.travel.admin.model.domain.Hotel;
import com.travel.admin.model.dto.hotel.HotelDto;
import com.travel.admin.model.valid.AddGroup;
import com.travel.admin.model.valid.UpdateGroup;
import com.travel.admin.service.ExcelService;
import com.travel.admin.service.HotelService;
import com.travel.admin.service.RoomService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * Hotel-related API
 */
@RestController
@RequestMapping("/hotel")
public class HotelController {
    @Resource
    private HotelService hotelService;
    @Resource
    private RoomService roomService;
    @Resource
    private ExcelService excelService;

    /**
     * Paginated conditional query for the hotel list
     */
    @PostMapping("/page/{pageNum}/{pageSize}")
    public Result getPageByCondition(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                     @RequestBody Hotel hotel) {
        Page<Hotel> page = new Page<>(pageNum, pageSize);
        Page<Hotel> hotelPage = hotelService.getPageByCondition(page, hotel);
        return Result.success(hotelPage);
    }

    /**
     * Add a hotel
     */
    @PostMapping
    public Result addHotel(@Validated(AddGroup.class) @RequestBody HotelDto dto) {
        Hotel hotel = dto.convert();
        return hotelService.addHotel(hotel) ? Result.success("Hotel added successfully!") : Result.error("Failed to add hotel!");
    }

    /**
     * Delete a hotel
     */
    @DeleteMapping("/{id}")
    public Result deleteHotel(@Valid @PathVariable @NotNull(message="Delete hotel ID cannot be null") Integer id) {
        return hotelService.deleteHotelById(id) ? Result.success("Hotel deleted successfully!") : Result.error("Failed to delete hotel!");
    }

    /**
     * Modify a hotel
     */
    @PutMapping
    public Result updateHotel(@Validated(UpdateGroup.class) @RequestBody HotelDto dto) {
        Hotel hotel = dto.convert();
        return hotelService.updateHotel(hotel) ? Result.success("Hotel updated successfully!") : Result.error("Failed to update hotel!");
    }

    /**
     * Query hotel details
     */
    @GetMapping("/{id}")
    public Result getHotel(@PathVariable Integer id) {
        return Result.success(hotelService.getHotelById(id));
    }

    /**
     * Upload Excel file to populate hotel data
     */
    @PostMapping("/import")
    public Result importExcel(MultipartFile file) throws IOException {
        excelService.importExcel(new HotelExcelImportListener(hotelService, roomService), file);
        return Result.success("Hotel data uploaded successfully");
    }

    /**
     * Download hotel Excel data
     */
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<Hotel> hotelList = hotelService.getList();
        excelService.exportExcel(hotelList, response);
    }
}
