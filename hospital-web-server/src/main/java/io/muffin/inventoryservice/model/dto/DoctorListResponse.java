package io.muffin.inventoryservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DoctorListResponse {

    private List<DoctorCardResponse> content;
    private int pageNo;
    private int pageSize;
    private int totalElements;
    private int totalPages;
}
