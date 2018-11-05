package com.service.rest.reactive.spring5webfluxrest.controller;

import com.service.rest.reactive.spring5webfluxrest.domain.Vendor;
import com.service.rest.reactive.spring5webfluxrest.repository.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @Before
    public void setUp() throws Exception {

        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void getVendors() {

        //Behaviour drive mockito.
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(
                        Vendor.builder().firstName("firstname1").lastName("lastname1").build(),
                        Vendor.builder().firstName("firstname2").lastName("lastname2").build()));
        webTestClient
                .get()
                .uri(getBaseUrl())
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getVendorById() {

        BDDMockito.given(vendorRepository.findById("SomeString"))
                .willReturn(Mono.just(Vendor.builder().firstName("firstname").lastName("lastname").build()));

        webTestClient
                .get()
                .uri(getBaseUrl()+"SomeString")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(1);
    }

    private String getBaseUrl() {
        return VendorController.BASE_URL;
    }
}