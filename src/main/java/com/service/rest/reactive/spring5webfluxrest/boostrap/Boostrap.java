package com.service.rest.reactive.spring5webfluxrest.boostrap;

import com.service.rest.reactive.spring5webfluxrest.domain.Vendor;
import com.service.rest.reactive.spring5webfluxrest.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class Boostrap implements CommandLineRunner {


    public Boostrap(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    private VendorRepository vendorRepository;


    @Override
    public void run(String... args) throws Exception {
        loadVendorData();
    }

    private void loadVendorData() {

        Vendor vendor1 = new Vendor();
        vendor1.setFirstName("Vendor 1");
        vendor1.setLastName("lastname 1");

        Vendor vendor2 = new Vendor();
        vendor2.setFirstName("Vendor 2");
        vendor2.setLastName("lastname 2");

        vendorRepository.saveAll(Arrays.asList(vendor1, vendor2)).blockLast();

        log.info("Loaded vendor data");
    }
}
