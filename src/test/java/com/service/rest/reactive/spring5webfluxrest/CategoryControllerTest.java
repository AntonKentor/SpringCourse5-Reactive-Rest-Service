package com.service.rest.reactive.spring5webfluxrest;

import com.service.rest.reactive.spring5webfluxrest.controller.CategoryController;
import com.service.rest.reactive.spring5webfluxrest.domain.Category;
import com.service.rest.reactive.spring5webfluxrest.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                .uri(getBaseUri()+"SomeString")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(1);
    }

    public String getBaseUri() {
        return CategoryController.BASE_URL;
    }
}