package com.example.hacken.api.controller;

import com.example.hacken.api.service.FileUploadService;
import com.example.hacken.api.service.HealthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
@AllArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public Boolean handleFileUpload(@RequestParam("file") MultipartFile file) {
        return fileUploadService.uploadFile(file.get);
    }
}
