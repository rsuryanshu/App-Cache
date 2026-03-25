package com.suryanshu.App_Cache.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductPageDTO {
    private List<ProductDTO> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public ProductPageDTO(List<ProductDTO> data, int page, int size,
                          long totalElements, int totalPages) {
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

}
