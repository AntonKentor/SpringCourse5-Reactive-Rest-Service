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


        Category category1 = Category.builder().description("Description 1").build();
        Category category2 = Category.builder().description("Description 2").build();

        categoryRepository.saveAll(Arrays.asList(category1, category2)).blockLast();
        log.info("Loaded category data");
    }

    private void loadVendorData() {

        Vendor vendor1 = Vendor.builder().firstName("Vendor 1").lastName("lastname 1").build();
        Vendor vendor2 = Vendor.builder().firstName("Vendor 2").lastName("lastname 2").build();

        vendorRepository.saveAll(Arrays.asList(vendor1, vendor2)).blockLast();

        log.info("Loaded vendor data");
    }
}
