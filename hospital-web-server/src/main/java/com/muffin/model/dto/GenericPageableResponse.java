package com.muffin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenericPageableResponse<T> {

    private List<T> content;
    private int pageNo;
    private int pageSize;
    private Long totalElements;
    private int totalPages;
}
