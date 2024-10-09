package com.travel.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.admin.model.Result;
import com.travel.admin.model.domain.Reserve;
import com.travel.admin.model.dto.reserve.ReserveDto;
import com.travel.admin.model.valid.AddGroup;
import com.travel.admin.model.valid.UpdateGroup;
import com.travel.admin.service.ReserveService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Reservation-related API
 */
@RestController
@RequestMapping("/reserve")
public class ReserveController {
    @Resource
    private ReserveService reserveService;

    /**
     * Paginated conditional query for the reservation list
     */
    @PostMapping("/page/{pageNum}/{pageSize}")
    public Result getPageByCondition(@PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                     @RequestBody Reserve reserve) {
        Page<Reserve> page = new Page<>(pageNum, pageSize);
        Page<Reserve> reservePage = reserveService.getPageByCondition(page, reserve);
        return Result.success(reservePage);
    }

    /**
     * Reserve a room
     */
    @PostMapping
    public Result addReserve(@Validated(AddGroup.class) @RequestBody ReserveDto dto) {
        Reserve reserve = dto.convert();
        return reserveService.addReserve(reserve, dto.getCode()) ? Result.success("Reservation added successfully!") : Result.error("Failed to add reservation!");
    }

    /**
     * Delete reservation information
     */
    @DeleteMapping("/{id}")
    public Result deleteReserve(@Valid @PathVariable @NotNull(message="Delete reservation ID cannot be null") Integer id) {
        return reserveService.deleteReserveById(id) ? Result.success("Reservation deleted successfully!") : Result.error("Failed to delete reservation!");
    }

    /**
     * Modify reservation information
     */
    @PutMapping
    public Result updateReserve(@Validated(UpdateGroup.class) @RequestBody ReserveDto dto) {
        Reserve reserve = dto.convert();
        return reserveService.updateReserve(reserve) ? Result.success("Reservation updated successfully!") : Result.error("Failed to update reservation!");
    }

    /**
     * Query all reservations under the merchant's hotel
     */
    @GetMapping("/page/all/{userId}/{pageNum}/{pageSize}")
    public Result getReserveAllByUserId(@PathVariable("userId") Integer userId, @PathVariable Integer pageNum,
                                        @PathVariable Integer pageSize) {
        Page<Reserve> page = new Page<>(pageNum, pageSize);
        Page<Reserve> reservePage = reserveService.getReserveAllByUserId(page, userId);
        return Result.success(reservePage);
    }
}

