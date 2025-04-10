package com.proyect.mvp.application.services;


import com.proyect.mvp.application.dtos.create.ProductCreateDTO;
import com.proyect.mvp.application.dtos.update.ProductUpdateDTO;
import com.proyect.mvp.domain.model.entities.NeighborhoodEntity;
import com.proyect.mvp.domain.model.entities.ProductEntity;
import com.proyect.mvp.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
   
    private final UserService userService;
    private final NeighborhoodService neighborhoodService;


    public ProductService(ProductRepository productRepository, UserService userService, NeighborhoodService neighborhoodService) {
        this.productRepository = productRepository;
        this.neighborhoodService = neighborhoodService;
        this.userService = userService;
   
    }



    public Flux<ProductEntity> getAllProductsFilterByName(String name) {
        return productRepository.findAllFilterByName(name);
    }
    public Flux<ProductEntity> getAllProducts() {
        return productRepository.findAll();
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
                .photo(productDTO.getPhoto())
                .unitMeasurement(productDTO.getUnitMeasurement())
                
                .unity_price(productDTO.getUnity_price())
                
                .build();
        
        return productRepository.save(product);
    }

    

    public Mono<ProductEntity> updateProduct(ProductUpdateDTO productDTO) {
        return productRepository.findById(productDTO.getIdProduct())
                .flatMap(existingProduct -> {
                    ProductEntity newProduct = ProductEntity.builder()
                            .idProduct(existingProduct.getIdProduct())
                            .name(productDTO.getName())
                            .photo(productDTO.getPhoto())
                            .unitMeasurement(productDTO.getUnitMeasurement())
                            
                            .build();
                    return productRepository.save(newProduct);
                });
    }

   

   
    


    
}