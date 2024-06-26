package com.example.hacken.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    private String searchValue;
    private Integer page;
    private Integer perPage;
}
