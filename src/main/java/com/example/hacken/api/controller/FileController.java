package com.example.hacken.api.controller;

import com.example.hacken.api.dto.SearchDto;
import com.example.hacken.api.entity.FileData;
import com.example.hacken.api.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<Void> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        FileData fileData = fileService.uploadFile(file.getOriginalFilename(), new BufferedReader(new InputStreamReader(file.getInputStream())));

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(fileData.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/search")
    public List<FileData> handleFileUpload(@RequestBody SearchDto search) {
        return fileService.search(search.getSearchValue(), search.getPerPage(), search.getPage());
    }
}
