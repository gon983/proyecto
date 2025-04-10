package com.proyect.mvp.application.services;



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
    
}
