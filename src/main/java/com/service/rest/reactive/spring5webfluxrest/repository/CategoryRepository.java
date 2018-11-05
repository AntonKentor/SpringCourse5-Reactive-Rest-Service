package com.service.rest.reactive.spring5webfluxrest.repository;

import com.service.rest.reactive.spring5webfluxrest.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}
