package com.travel.admin.service;

import com.travel.admin.listener.HotelExcelImportListener;
import com.travel.admin.model.domain.Hotel;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public interface ExcelService {
    /**
     * Export
     */
    void exportExcel(List<Hotel> hotelList, HttpServletResponse response) throws IOException;

    /**
     * Import
     */
    void importExcel(HotelExcelImportListener listener, MultipartFile file) throws IOException;
}
