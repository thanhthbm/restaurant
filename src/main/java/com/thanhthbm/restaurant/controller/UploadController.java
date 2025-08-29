package com.thanhthbm.restaurant.controller;

import com.thanhthbm.restaurant.service.CloudinaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class UploadController {
    private final CloudinaryService cloudinaryService;

    public UploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestPart(value = "image", required = false) MultipartFile file,
            @RequestParam("folder") String folderName
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(this.cloudinaryService.uploadImage(file, folderName));
    }
}
