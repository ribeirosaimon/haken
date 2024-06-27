package com.example.hacken.service;

import com.example.hacken.api.entity.FileData;
import com.example.hacken.api.service.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

@TestComponent
public class FileServiceTest {

    private final FileService fileService = new FileService(null);

    @Test
    public void testFile1() throws IOException {
        String filePath = Objects.requireNonNull(getClass().getResource("/static/test1.csv")).getPath();
        FileData fileData = fileService.contructFileData("test1.csv", new BufferedReader(new FileReader(filePath)));
        Assertions.assertNotNull(fileData);
        Assertions.assertEquals("test1", fileData.getFileName());
        Assertions.assertEquals("csv", fileData.getFileType());
        Assertions.assertEquals(",", fileData.getDelimiter());
        Assertions.assertEquals("A", fileData.getIndexColumn().getFirst().getColumn());
    }

    @Test
    public void testFile2() throws IOException {
        String filePath = Objects.requireNonNull(getClass().getResource("/static/test2.csv")).getPath();
        FileData fileData = fileService.contructFileData("test2.csv", new BufferedReader(new FileReader(filePath)));
        Assertions.assertNotNull(fileData);
        Assertions.assertEquals("test2", fileData.getFileName());
        Assertions.assertEquals("csv", fileData.getFileType());
        Assertions.assertEquals("\t", fileData.getDelimiter());
        Assertions.assertEquals("A", fileData.getIndexColumn().getFirst().getColumn());
    }

    @Test
    public void testFile3() throws IOException {
        String filePath = Objects.requireNonNull(getClass().getResource("/static/test3.csv")).getPath();
        FileData fileData = fileService.contructFileData("test3.csv", new BufferedReader(new FileReader(filePath)));
        Assertions.assertNotNull(fileData);
        Assertions.assertEquals("test3", fileData.getFileName());
        Assertions.assertEquals("csv", fileData.getFileType());
        Assertions.assertEquals(";", fileData.getDelimiter());
        Assertions.assertEquals("Col2", fileData.getIndexColumn().get(1).getColumn());
    }

}
