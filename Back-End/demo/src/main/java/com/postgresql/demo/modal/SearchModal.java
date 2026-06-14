package com.postgresql.demo.modal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchModal {

    private String username;
    private String email;

    private int page = 0;
    private int size = 10;

    private String sortBy = "username";
    private String sortDirection = "asc";
}