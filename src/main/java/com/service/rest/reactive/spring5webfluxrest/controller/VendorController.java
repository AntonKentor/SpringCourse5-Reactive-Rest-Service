package com.service.rest.reactive.spring5webfluxrest.controller;

import com.service.rest.reactive.spring5webfluxrest.domain.Vendor;
import com.service.rest.reactive.spring5webfluxrest.repository.VendorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class VendorController {

    public static final String BASE_URL = "/api/v1/vendors/";

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    private final VendorRepository vendorRepository;

    @GetMapping(VendorController.BASE_URL)
    public Flux<Vendor> getVendors() {
        return vendorRepository.findAll();
    }

    @GetMapping(VendorController.BASE_URL + "{id}")
    public Mono<Vendor> getVendorById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }
}
