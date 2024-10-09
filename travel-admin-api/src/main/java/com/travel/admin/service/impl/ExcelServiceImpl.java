package com.travel.admin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.travel.admin.listener.HotelExcelImportListener;
import com.travel.admin.model.domain.Hotel;
import com.travel.admin.service.ExcelService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class ExcelServiceImpl implements ExcelService {
    @Override
    public void exportExcel(List<Hotel> hotelList, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("Hotel data_" + new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date()), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), Hotel.class).sheet("Hotel data").doWrite(hotelList);
    }

    @Override
    public void importExcel(HotelExcelImportListener listener, MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), Hotel.class, listener).sheet().doRead();
    }
}
