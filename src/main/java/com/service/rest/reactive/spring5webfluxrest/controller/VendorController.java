package com.service.rest.reactive.spring5webfluxrest.controller;

import com.service.rest.reactive.spring5webfluxrest.domain.Vendor;
import com.service.rest.reactive.spring5webfluxrest.repository.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(VendorController.BASE_URL)
public class VendorController {

    public static final String BASE_URL = "/api/v1/vendors/";

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    private final VendorRepository vendorRepository;

    @GetMapping
    public Flux<Vendor> getVendors() {
        return vendorRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono<Vendor> getVendorById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createVendor(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("{id}")
    Mono<Vendor> updateCategory(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("{id}")
    Mono<Vendor> patchVendor(@PathVariable String id, @RequestBody Vendor vendor) {

        Vendor foundVendor = vendorRepository.findById(id).block();

        /*
        * This logic should be extracted to servicelayer
        * */

        boolean firstNameChanged = false;
        boolean lastNameChanged = false;

        if (foundVendor.getFirstName() != vendor.getFirstName()) {
            foundVendor.setFirstName(vendor.getFirstName());
            firstNameChanged = true;
        }

        if (foundVendor.getLastName() != vendor.getLastName()) {
            foundVendor.setLastName(vendor.getLastName());
            lastNameChanged = true;
        }

        if (firstNameChanged || lastNameChanged) {
            return vendorRepository.save(foundVendor);
        }
        return Mono.just(foundVendor);
    }
}
