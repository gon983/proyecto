package com.proyect.mvp.application.services;


import com.proyect.mvp.dtos.create.ProductCreateDTO;
import com.proyect.mvp.dtos.update.ProductUpdateDTO;
import com.proyect.mvp.domain.model.entities.ProductEntity;
import com.proyect.mvp.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<ProductEntity> getProductsByProducer(UUID fkProductor) {
        return productRepository.findByFkProductor(fkProductor);
    }

    public Mono<ProductEntity> createProduct(ProductCreateDTO productDTO) {
        ProductEntity product = ProductEntity.builder()
                .idProduct(UUID.randomUUID())
                .name(productDTO.getName())
                .stock(productDTO.getStock())
                .alertStock(productDTO.getAlertStock())
                .photo(productDTO.getPhoto())
                .unitMeasurement(productDTO.getUnitMeasurement())
                .fkProductor(productDTO.getFkProductor())
                .build();
        
        return productRepository.insertProduct(product.getIdProduct(), product.getName(), product.getStock(), product.getAlertStock(), product.getPhoto(), product.getUnitMeasurement(), product.getFkProductor());
    }

    

    public Mono<ProductEntity> updateProduct(ProductUpdateDTO productDTO) {
        return productRepository.findById(productDTO.getIdProduct())
                .flatMap(existingProduct -> {
                    ProductEntity newProduct = ProductEntity.builder()
                            .idProduct(existingProduct.getIdProduct())
                            .name(productDTO.getName())
                            .stock(productDTO.getStock())
                            .alertStock(productDTO.getAlertStock())
                            .photo(productDTO.getPhoto())
                            .unitMeasurement(productDTO.getUnitMeasurement())
                            .fkProductor(existingProduct.getFkProductor())
                            .build();
                    return productRepository.save(newProduct);
                });
    }
}