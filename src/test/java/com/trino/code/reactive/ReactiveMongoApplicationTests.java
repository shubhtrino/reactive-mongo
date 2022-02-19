package com.trino.code.reactive;

import com.trino.code.reactive.controller.ProductController;
import com.trino.code.reactive.dto.ProductDto;
import com.trino.code.reactive.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class ReactiveMongoApplicationTests {

    @MockBean
    ProductService productService;

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void saveProductTest(){
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("123","Mob",12,500.0));
        when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);
        webTestClient.post().uri("/products/save")
                .body(Mono.just(productDtoMono),ProductDto.class)
                .exchange()
                .expectStatus().isOk();

    }


    @Test
    public void getAllProducts(){
        Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("234123","SdMob",12,500.0),new ProductDto("123","Mob",12,500.0));
        when(productService.getAllProducts()).thenReturn(productDtoFlux);
        Flux<ProductDto> responseBody = webTestClient.get().uri("/products/")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new ProductDto("234123","SdMob",12,500.0))
                .expectNext(new ProductDto("123","Mob",12,500.0))
                .verifyComplete();

    }

}
