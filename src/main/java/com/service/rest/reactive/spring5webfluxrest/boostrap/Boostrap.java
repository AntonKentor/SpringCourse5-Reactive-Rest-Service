package com.service.rest.reactive.spring5webfluxrest.boostrap;

import com.service.rest.reactive.spring5webfluxrest.domain.Category;
import com.service.rest.reactive.spring5webfluxrest.domain.Vendor;
import com.service.rest.reactive.spring5webfluxrest.repository.CategoryRepository;
import com.service.rest.reactive.spring5webfluxrest.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class Boostrap implements CommandLineRunner {

    public Boostrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;


    @Override
    public void run(String... args) throws Exception {

        if (categoryRepository.count().block() == 0) {
            loadCategoryData();
        }

        if (vendorRepository.count().block() == 0) {
            loadVendorData();
        }
    }

    private void loadCategoryData() {

        categoryRepository.saveAll(Arrays.asList(
                Category.builder().description("Fruits").build(),
                Category.builder().description("Nuts").build(),
                Category.builder().description("Breads").build(),
                Category.builder().description("Meats").build(),
                Category.builder().description("Eggs").build()
        )).blockLast();
        log.info("Loaded category data");
    }

    private void loadVendorData() {

        vendorRepository.saveAll(Arrays.asList(
                Vendor.builder().firstName("Tom").lastName("Cruise").build(),
                Vendor.builder().firstName("Hugh").lastName("Heffner").build(),
                Vendor.builder().firstName("Michael").lastName("Bolton").build(),
                Vendor.builder().firstName("Bradley").lastName("Cooper").build()
        )).blockLast();
        log.info("Loaded vendor data");
    }
}
