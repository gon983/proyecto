package com.proyect.mvp.application.dtos.create;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class RecommendedPackCreateDTO {
    private String name;
    private String description;
    private String imageUrl;
    private List<PackProductDTO> products;
}

