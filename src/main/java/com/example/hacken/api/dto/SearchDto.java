package com.example.hacken.api.dto;

import com.example.hacken.api.entity.FileData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class SearchDto {
    private String searchValue;
    private Integer page;
    private Integer perPage;

    @Getter
    @Setter
    public static class SearchResultDto{
        private Long total;
        private Long page;
        private Long perPage;
        private List<FileData> results = new ArrayList<>();
    }
}
