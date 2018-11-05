package com.service.rest.reactive.spring5webfluxrest;


import com.service.rest.reactive.spring5webfluxrest.controller.CategoryController;
import com.service.rest.reactive.spring5webfluxrest.domain.Category;
import com.service.rest.reactive.spring5webfluxrest.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

public class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @Before
    public void setUp() throws Exception {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void getCategories() throws Exception {

        //Behavior driven mockito.
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(
                        Flux.just(Category.builder().description("Cat1").build(),
                                Category.builder().description("Cat2").build()));
        webTestClient
                .get()
                .uri(getBaseUri())
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    public void getCategoryById() {

        BDDMockito.given(categoryRepository.findById("SomeString"))
                .willReturn(Mono.just(Category.builder().description("Cat").build()));

        webTestClient
                .get()
                .uri(getBaseUri() + "SomeString")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(1);
    }

    @Test
    public void createNewCategory() {

        BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just(Category.builder().description("some cat").build());

        webTestClient
                .post()
                .uri(getBaseUri())
                .body(catToSaveMono, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    public String getBaseUri() {
        return CategoryController.BASE_URL;
    }
}