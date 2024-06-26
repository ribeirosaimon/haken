package com.example.hacken.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileData {

    @Id
    private ObjectId id;

    private String fileName;

    private String fileType;

    private String delimiter;

    private List<IndexColumn> indexColumn;

    private List<IndexRow> indexRow;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class IndexRow{
        private Integer index;
        private String rows;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class IndexColumn{
        private Integer index;
        private String column;
    }
}
