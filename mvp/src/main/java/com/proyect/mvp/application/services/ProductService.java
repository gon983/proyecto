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



    public Flux<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public Flux<ProductEntity> getProductsByProducer(UUID fkProductor) {
        return productRepository.findByFkProductor(fkProductor);
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

    public Flux<ProductEntity> getOptionsForStandarProduct(UUID fkStandarProduct, UUID idUser){
        return userService.getLocality(idUser)
                           .flatMapMany(locality -> productRepository.findByFkStandarProductAndFkLocality(fkStandarProduct, locality));
    } 

    public Flux<NeighborhoodEntity> getNeighboordsThatCouldBuyTheProduct(UUID idLocality){
        return neighborhoodService.getNeighborhoodsOfLocality(idLocality);
    }

    public Flux<UUID> getLocalitiesWhereProducerSell(List<ProductEntity> productList){
        return Flux.fromStream(productList.stream()
                          .map(product -> product.getFkLocality())
                          .distinct());
    }


    
}