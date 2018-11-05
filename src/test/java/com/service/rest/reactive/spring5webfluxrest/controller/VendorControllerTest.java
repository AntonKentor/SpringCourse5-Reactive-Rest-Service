package com.service.rest.reactive.spring5webfluxrest.controller;

import com.service.rest.reactive.spring5webfluxrest.domain.Vendor;
import com.service.rest.reactive.spring5webfluxrest.repository.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
        given(vendorRepository.findAll())
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

        given(vendorRepository.findById("SomeString"))
                .willReturn(Mono.just(Vendor.builder().firstName("firstname").lastName("lastname").build()));

        webTestClient
                .get()
                .uri(getBaseUrl() + "SomeString")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(1);
    }

    @Test
    public void createNewVendor() {

        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().firstName("fistname").lastName("lastname").build());

        webTestClient
                .post()
                .uri(getBaseUrl())
                .body(vendorToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdateVendor() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName("firstname").lastName("lastname").build());

        webTestClient
                .put()
                .uri(getBaseUrl() + "someid")
                .body(vendorToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void testPatchVendor() {

        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Some firstname").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> venToPatchMono = Mono.just(Vendor.builder().build());

        webTestClient
                .patch()
                .uri(getBaseUrl() + "someid")
                .body(venToPatchMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository, times(1)).save(any());
    }

    @Test
    public void testPatchVendorNoChanges() {

        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> venToPatchMono = Mono.just(Vendor.builder().build());

        webTestClient
                .patch()
                .uri(getBaseUrl() + "someid")
                .body(venToPatchMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository, never()).save(any());
    }

    private String getBaseUrl() {
        return VendorController.BASE_URL;
    }
}