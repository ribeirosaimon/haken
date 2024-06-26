package com.example.hacken.api.service;

import com.example.hacken.api.entity.FileData;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FileService {

    private final MongoTemplate mongoTemplate;

    public FileService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<FileData> search(String value, Integer limit, Integer skip){
        return this.searchInFile(value,limit,skip);
    }

    public FileData uploadFile(String originalFilename, BufferedReader reader) throws IOException {
        String delimiter = this.foundDelimiter(reader);
        FileData fileData = this.readCsvFile(originalFilename, reader, delimiter);

        return this.mongoTemplate.save(fileData);
    }

    private FileData readCsvFile(String originalFilename, BufferedReader reader, String delimiter) throws IOException {
        List<FileData.IndexRow> rows = new ArrayList<>();
        List<FileData.IndexColumn> columns = new ArrayList<>();

        String line;
        int vIndex = 0;
        while ((line = reader.readLine()) != null) {
            if (vIndex == 0) {
                String[] split = line.split(delimiter);
                columns = IntStream.range(0, split.length)
                        .mapToObj(i -> new FileData.IndexColumn(i, split[i]))
                        .toList();
            } else {
                FileData.IndexRow file = new FileData.IndexRow(vIndex, line);

                rows.add(file);
            }
            vIndex ++;
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

        return file;
    }

    private String foundDelimiter(BufferedReader reader) throws IOException {
        String[] delimiter = new String[4];
        for (int i = 0; i <delimiter.length -1 ; i++) {
            String line;
            if ((line = reader.readLine()) != null) {
                String s = this.foundBetterDelimiterMatch(line);
                if (Arrays.asList(delimiter).contains(s)) return s;
                delimiter[i] = s;
            }
        }

        throw new IOException();
    }


    private String foundBetterDelimiterMatch(String line) {
        String[] delimiter = {";", ",", "\\|", "\n"};

        int value = 0;
        int delimiterIndex = 0;

        for (int i = 0; i < delimiter.length; i++) {
            String[] split = line.split(delimiter[i]);
            if (value <= split.length){
                value = split.length;
                delimiterIndex = i;
            }
        }

        return delimiter[delimiterIndex];

    }

    private List<FileData> searchInFile(String value, Integer limit, Integer skip) {
        AggregationOperation addFields = Aggregation.addFields()
                .addField("indexRowsArray")
                .withValueOf("$objectToArray:$indexRows").build();

        AggregationOperation match = Aggregation.match(Criteria.where("indexRowsArray.v").regex(value));
        Aggregation aggregation = Aggregation.newAggregation(addFields, match, Aggregation.limit(limit), Aggregation.skip(skip));

        return mongoTemplate.aggregate(aggregation, "fileData", FileData.class).getMappedResults();
    }
}
