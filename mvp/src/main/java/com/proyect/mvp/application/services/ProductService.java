package com.proyect.mvp.application.services;


import com.proyect.mvp.domain.model.dtos.create.ProductCreateDTO;
import com.proyect.mvp.domain.model.dtos.update.ProductUpdateDTO;
import com.proyect.mvp.domain.model.entities.ProductEntity;
import com.proyect.mvp.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductHistoryService productHistoryService;

    public ProductService(ProductRepository productRepository, ProductHistoryService productHistoryService) {
        this.productRepository = productRepository;
        this.productHistoryService = productHistoryService;
    }



    public Flux<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public Flux<ProductEntity> getProductsByProducer(UUID fkProductor) {
        return productRepository.findByFkProductor(fkProductor)
                .flatMap(product -> {
                    return productHistoryService.getProductHistory(product.getIdProduct())
                            .collectList()
                            .flatMap(historyList -> {
                                product.addHistory(historyList);
                                return Mono.just(product);
                            });
                });
    }

    public Mono<ProductEntity> getProductByIdWithHistory(UUID id) {
        return productRepository.findById(id)
                .flatMap(product -> {
                    return productHistoryService.getProductHistory(product.getIdProduct())
                            .collectList()
                            .flatMap(historyList -> {
                                product.addHistory(historyList);
                                return Mono.just(product);
                            });
                });
    }

    public Mono<ProductEntity> getProductById(UUID id){
        return productRepository.findById(id);
    }

    public Mono<Void> updateStock(UUID idProduct, double newStock){
        return productRepository.updateStock(idProduct, newStock);

    }


    public Mono<ProductEntity> createProduct(ProductCreateDTO productDTO) {
        ProductEntity product = ProductEntity.builder()
                .name(productDTO.getName())
                .stock(productDTO.getStock())
                .alertStock(productDTO.getAlertStock())
                .photo(productDTO.getPhoto())
                .unitMeasurement(productDTO.getUnitMeasurement())
                .fkProductor(productDTO.getFkProductor())
                .unity_price(productDTO.getUnity_price())
                .fkLocality(productDTO.getFkLocality())
                .fkStandarProduct(productDTO.getFkStandarProduct())
                .build();
        
        return productRepository.save(product);
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