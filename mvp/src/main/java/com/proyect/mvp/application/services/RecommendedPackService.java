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

    public Mono<RecommendedPackEntity> createPack(RecommendedPackCreateDTO packDTO) {
    return Mono.usingWhen(
        packRepository.save(RecommendedPackEntity.builder()
                .name(packDTO.getName())
                .description(packDTO.getDescription())
                .imageUrl(packDTO.getImageUrl())
                .build()),
        savedPack -> Flux.fromIterable(packDTO.getProducts())
                .flatMap(packProduct -> productXPackRepository.save(
                        ProductXRecommendedPackEntity.builder()
                                .fkRecommendedPack(savedPack.getIdRecommendedPack())
                                .fkProduct(packProduct.getProductId())
                                .quantity(packProduct.getQuantity())
                                .build()))
                .then(Mono.just(savedPack)),
        savedPack -> Mono.empty()
    );
}
    
}
