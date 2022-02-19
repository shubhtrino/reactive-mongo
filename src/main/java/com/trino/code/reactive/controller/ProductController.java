package com.trino.code.reactive.controller;

import com.trino.code.reactive.dto.ProductDto;
import com.trino.code.reactive.entity.Product;
import com.trino.code.reactive.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("")
    public Flux<ProductDto> getAllProducts(){
        return  productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> getProductById(@PathVariable  String id){
        return  productService.getProductById(id);
    }

    @GetMapping("/range")
    public Flux<ProductDto> getProductInRange(@RequestParam("min") double min,@RequestParam("max") double max){
        return  productService.getProductsInRange(min,max);
    }

    @PostMapping("/save")
    public Mono<ProductDto> saveProduct(@RequestBody  Mono<ProductDto> productDto){
        return  productService.saveProduct(productDto);
    }

    @PutMapping("update/{id}")
    public Mono<ProductDto> updateProduct(@RequestBody  Mono<ProductDto> productDto,@PathVariable  String id){
        return  productService.updateProduct(productDto,id);
    }

    @DeleteMapping("delete/{id}")
    public Mono<Void> deleteProduct(@PathVariable  String id){
        return  productService.deleteProduct(id);
    }
}
