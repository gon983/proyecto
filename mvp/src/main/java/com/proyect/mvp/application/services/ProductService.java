package com.proyect.mvp.application.services;


import com.proyect.mvp.application.dtos.create.ProductCreateDTO;
import com.proyect.mvp.application.dtos.update.ProductContaduryUpdateDTO;
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
    private final CategoryService categoryService;
   

  


    public ProductService(ProductRepository productRepository, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
       
   
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
                
               
                .fkCategory(productDTO.getFkCategory())
                
                .build();
        
        return productRepository.save(product);
    }

  

    

    public Mono<ProductEntity> putProduct(ProductUpdateDTO productDTO) {
        return productRepository.findById(productDTO.getIdProduct())
                .flatMap(existingProduct -> {
                    ProductEntity newProduct = ProductEntity.builder()
                            .idProduct(existingProduct.getIdProduct())
                            .name(productDTO.getName())
                            .photo(productDTO.getPhoto())
                            .unitMeasurement(productDTO.getUnitMeasurement())
                            .fkCategory(productDTO.getFkCategory())
                            
                            .build();
                    return productRepository.save(newProduct);
                });
    }

    public Mono<ProductEntity> actualizarContaduriaProducto(ProductContaduryUpdateDTO dto) {
        return productRepository.findById(dto.getIdProduct())
                .flatMap(existingProduct -> {
                    ProductEntity newProduct = ProductEntity.builder()
                            .idProduct(existingProduct.getIdProduct())
                            .unity_price(dto.getUnity_price())
                            .unityCost(dto.getUnityCost())
                            .build();
                    return productRepository.save(newProduct);
                });
    }

    public Flux<ProductEntity> getAllProductsFilterByCategory(UUID id_category){
        return productRepository.findAllFilterByCategory(id_category);
    }

   

   
    


    
}