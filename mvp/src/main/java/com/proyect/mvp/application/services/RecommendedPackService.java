package com.proyect.mvp.application.services;



import com.proyect.mvp.application.dtos.create.RecommendedPackCreateDTO;
import com.proyect.mvp.domain.model.entities.ProductXRecommendedPackEntity;
import com.proyect.mvp.domain.model.entities.RecommendedPackEntity;
import com.proyect.mvp.domain.repository.RecommendedPackRepository;
import com.proyect.mvp.domain.repository.ProductXRecommendedPackRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
                    )
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
                    )
                    .collectList()
                    .map(products -> {
                        pack.setProducts(products);
                        return pack;
                    })
            );
    }
    

    public Mono<RecommendedPackEntity> createPack(RecommendedPackCreateDTO packDTO) {
        return packRepository.save(toEntity(UUID.randomUUID(), packDTO.getName(), packDTO.getDescription(), packDTO.getImageUrl()))
            .flatMap(savedPack ->
                         Flux.fromIterable(packDTO.getProducts())
                             .flatMap(packProduct ->productXPackRepository.save(
                                                                                ProductXRecommendedPackEntity.builder()
                                                                                    .fkRecommendedPack(packProduct.getProductId())
                                                                                    .fkProduct(packProduct.getProductId())
                                                                                    .quantity(packProduct.getQuantity())
                                                                                    .build()))
                            .collectList() // ← Esperamos a que se guarden todas
                             .thenReturn(savedPack) // ← Devolvemos el pack guardado
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
    
    
    
}
