package com.proyect.mvp.application.services;



import com.proyect.mvp.application.dtos.create.PackProductDTO;
import com.proyect.mvp.application.dtos.create.RecommendedPackCreateDTO;
import com.proyect.mvp.application.dtos.response.PackProductResponseDTO;
import com.proyect.mvp.application.dtos.update.PackProductAddDTO;
import com.proyect.mvp.application.dtos.update.PackProductDeleteDTO;
import com.proyect.mvp.application.dtos.update.PackProductEditDTO;
import com.proyect.mvp.domain.model.entities.ProductXRecommendedPackEntity;
import com.proyect.mvp.domain.model.entities.RecommendedPackEntity;
import com.proyect.mvp.domain.repository.RecommendedPackRepository;
import com.proyect.mvp.domain.repository.ProductXRecommendedPackRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class RecommendedPackService {
    private final RecommendedPackRepository packRepository;
    private final ProductXRecommendedPackRepository productXPackRepository;
    private final ProductService productService;

    public RecommendedPackService(RecommendedPackRepository packRepository, 
                                ProductXRecommendedPackRepository productXPackRepository,
                                ProductService productService) {
        this.packRepository = packRepository;
        this.productXPackRepository = productXPackRepository;
        this.productService = productService;
    }

    public Flux<RecommendedPackEntity> getAllPacks() {
        return packRepository.findAll();
    }

    public Mono<RecommendedPackEntity> getPackWithProducts(UUID packId) {
        return packRepository.findByIdRecommendedPack(packId)
            .flatMap(pack ->
                productXPackRepository.findAllByPackId(packId)
                    .flatMap(productXPack ->
                        productService.getProductById(productXPack.getFkProduct())
                        .map(product -> 
                        PackProductResponseDTO.builder()
                                                .quantity(productXPack.getQuantity())
                                                .productId(product.getIdProduct())
                                                .name(product.getName())
                                                .price(product.calculatePrice(productXPack.getQuantity()))
                                                .build()))
                    
                    .collectList()
                    .map(products -> {
                        pack.setProducts(products);
                        return pack;
                    })
            );
    }

    public Flux<RecommendedPackEntity> getAllPacksWithProducts() {
        return packRepository.findAll()
            .flatMap(pack ->
                productXPackRepository.findAllByPackId(pack.getIdRecommendedPack())
                    .flatMap(productXPack ->
                        productService.getProductById(productXPack.getFkProduct())
                                      .map(product -> 
                                            PackProductResponseDTO.builder()
                                                                    .quantity(productXPack.getQuantity())
                                                                    .productId(product.getIdProduct())
                                                                    .name(product.getName())
                                                                    .price(product.calculatePrice(productXPack.getQuantity()))
                                                                    .build()))

                    .collectList()
                    .map(products -> {
                        pack.setProducts(products);
                        return pack;
                    })
            );
    }
    

    public Mono<Void> createPack(RecommendedPackCreateDTO packDTO) {
        UUID idPack = UUID.randomUUID();
        return packRepository.save(toEntity(idPack, packDTO.getName(), packDTO.getDescription(), packDTO.getImageUrl()))
            .flatMapMany(savedPack -> savePacksProducts(idPack, packDTO.getProducts()))
            .then();
                                                         
    }

    

    public Flux<ProductXRecommendedPackEntity> savePacksProducts(UUID idPack, List<PackProductDTO> products ){
        return Flux.fromIterable(products)
        .flatMap(
            product -> {
                ProductXRecommendedPackEntity productPack = ProductXRecommendedPackEntity.builder()
                                                                                        .fkRecommendedPack(idPack)
                                                                                        .fkProduct(product.getProductId())
                                                                                        .quantity(product.getQuantity())
                                                                                        .build();
                return productXPackRepository.save(productPack);
            }
        );

    }




    public RecommendedPackEntity toEntity(UUID idRecommendedPack, String name, String description, String imageUrl) {
        return RecommendedPackEntity.builder()
                .idRecommendedPack(idRecommendedPack)
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .build();
    }


    public  Mono<RecommendedPackEntity> editarPack(PackProductEditDTO dto){
        return packRepository.findById(dto.getIdRecommendedPack()).flatMap(
            existingPack ->
            {
                existingPack.setName(dto.getName());
                existingPack.setDescription(dto.getDescription());
                existingPack.setImageUrl(dto.getImageUrl());
                return packRepository.save(existingPack);
            }
        );

    }

    public Mono<ProductXRecommendedPackEntity> agregarProductosAlPack(PackProductAddDTO dto){
        ProductXRecommendedPackEntity packProduct = ProductXRecommendedPackEntity.builder()
                                                                                 .idProductXRecommendedPack(UUID.randomUUID())
                                                                                 .fkRecommendedPack(dto.getIdPack())
                                                                                 .fkProduct(dto.getProductId())
                                                                                 .quantity(dto.getQuantity())
                                                                                 .build();
        return productXPackRepository.save(packProduct);

    }

    public Mono<Void> quitarProductoDelPack(PackProductDeleteDTO dto){
        return productXPackRepository.quitarProductoDelPack(dto.getIdPack(), dto.getIdProduct());
    }


    
    
    
}
