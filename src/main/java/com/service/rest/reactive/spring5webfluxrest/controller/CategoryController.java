package com.service.rest.reactive.spring5webfluxrest.controller;

import com.service.rest.reactive.spring5webfluxrest.domain.Category;
import com.service.rest.reactive.spring5webfluxrest.repository.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(CategoryController.BASE_URL)
public class CategoryController {

    public static final String BASE_URL = "/api/v1/categories/";

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private final CategoryRepository categoryRepository;

    @GetMapping
    public Flux<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono<Category> getCategoryById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Void> createCategory(@RequestBody Publisher<Category> categoryStream) {
        return categoryRepository.saveAll(categoryStream).then();
    }

    @PutMapping("{id}")
    Mono<Category> updateCategory(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping("{id}")
    Mono<Category> patchCategory(@PathVariable String id, @RequestBody Category category) {

        Category foundCategory = categoryRepository.findById(id).block();

        /*
         * This logic should be extracted to servicelayer
         * */

        if (foundCategory.getDescription() != category.getDescription()) {
            foundCategory.setDescription(category.getDescription());
            return categoryRepository.save(foundCategory);
        }
        return Mono.just(foundCategory);
    }

}
