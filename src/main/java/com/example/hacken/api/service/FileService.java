package com.example.hacken.api.service;

import com.example.hacken.api.dto.SearchDto;
import com.example.hacken.api.entity.FileData;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class FileService {

    private final MongoTemplate mongoTemplate;

    public FileService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public SearchDto.SearchResultDto search(String value, Integer perPage, Integer page) {
        if (perPage.equals(0)) perPage = 1;
        List<FileData> fileData = this.searchInFile(value, perPage, page - 1);
        Long count = this.countInFile(value);

        SearchDto.SearchResultDto searchResultDto = new SearchDto.SearchResultDto();
        searchResultDto.setResults(fileData);
        searchResultDto.setTotal(count);
        searchResultDto.setPage((long) page);
        searchResultDto.setPerPage((long) fileData.size());
        return searchResultDto;
    }

    public FileData uploadFile(String originalFilename, BufferedReader reader) throws IOException {
        FileData fileData = this.contructFileData(originalFilename, reader);
        return this.mongoTemplate.save(fileData);
    }

    public FileData contructFileData(String originalFilename, BufferedReader reader) throws IOException{
        Pair<String, String> columnAndDelimiter = this.foundDelimiter(reader);
        return this.readCsvFile(originalFilename, reader, columnAndDelimiter.getSecond(), columnAndDelimiter.getFirst());
    }

    private FileData readCsvFile(String originalFilename, BufferedReader reader, String delimiter, String column) throws IOException {
        List<FileData.IndexRow> rows = new ArrayList<>();

        String[] columnIndex = column.split(delimiter);
        List<FileData.IndexColumn> columns = IntStream.range(0, columnIndex.length)
                .mapToObj(i -> new FileData.IndexColumn(i, columnIndex[i]))
                .toList();

        int vIndex = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            FileData.IndexRow file = new FileData.IndexRow(vIndex, line);
            rows.add(file);
            vIndex++;
        }
        FileData file = new FileData();
        int lastDotIndex = originalFilename.lastIndexOf('.');

        String name = originalFilename.substring(0, lastDotIndex);
        String extension = originalFilename.substring(lastDotIndex + 1);

        file.setFileName(name);
        file.setFileType(extension);
        file.setIndexColumn(columns);
        file.setIndexRow(rows);
        file.setDelimiter(delimiter);
        file.setCreatedAt(LocalDateTime.now());

        return file;
    }

    private Pair<String, String> foundDelimiter(BufferedReader reader) throws IOException {
        String line;
        if ((line = reader.readLine()) != null) {
            return Pair.of(line, this.foundBetterDelimiterMatch(line));
        }

        throw new IOException();
    }


    private String foundBetterDelimiterMatch(String line) {
        String[] delimiter = {";", ",", "\\|", "\n", "\t"};

        int value = 0;
        int delimiterIndex = 0;

        for (int i = 0; i < delimiter.length; i++) {
            String[] split = line.split(delimiter[i]);
            if (value <= split.length) {
                value = split.length;
                delimiterIndex = i;
            }
        }

        return delimiter[delimiterIndex];

    }

    private List<FileData> searchInFile(String value, Integer limit, Integer skip) {
        Query query = new Query();
        query.limit(limit);
        query.skip(skip);
        Criteria criteria = this.createCriteria(value);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, FileData.class);
    }

    private Long countInFile(String value) {
        Query query = new Query();
        Criteria criteria = this.createCriteria(value);
        query.addCriteria(criteria);
        return mongoTemplate.count(query, FileData.class);
    }

    private Criteria createCriteria(String value) {
        return new Criteria().orOperator(
                Criteria.where("indexColumn.column").regex(value, "i"),
                Criteria.where("indexRow.rows").regex(value, "i")
        );
    }

    public FileData findFile(String idString) {
        return mongoTemplate.findById(idString, FileData.class);
    }
}
