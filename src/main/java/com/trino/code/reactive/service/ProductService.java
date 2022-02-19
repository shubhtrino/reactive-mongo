package com.trino.code.reactive.service;

import com.trino.code.reactive.dto.ProductDto;
import com.trino.code.reactive.repository.ProductRepo;
import com.trino.code.reactive.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;

    public Flux<ProductDto> getAllProducts(){
        return productRepo.findAll().map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> getProductById(String id){
        return productRepo.findById(id).map(AppUtils::entityToDto);
    }

    public  Flux<ProductDto> getProductsInRange(double min, double max){
        return  productRepo.findByPriceBetween(Range.closed(min,max));
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono){
        return productDtoMono.map(AppUtils::dtoToEntity).
                flatMap(productRepo::insert)
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id){
        return productRepo.findById(id)
                .flatMap(product -> productDtoMono.map(AppUtils::dtoToEntity)
                .doOnNext(p -> p.setId(id)))
                .flatMap(productRepo::save)
                .map(AppUtils::entityToDto);
    }

    public Mono<Void> deleteProduct(String id){
          return productRepo.deleteById(id);
    }

}
