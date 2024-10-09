package com.travel.admin.controller;

import com.travel.admin.constant.TravelAdminProperties;
import com.travel.admin.model.Result;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * File upload api
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    private TravelAdminProperties properties;

    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extendName = FileNameUtil.extName(originalFilename);
        String newFileName= IdUtil.simpleUUID() + "." + extendName;
        file.transferTo(new File(properties.getUpload().getPath() + newFileName));
        return Result.success("Upload Successfully","/api/image/" + newFileName);
    }
}